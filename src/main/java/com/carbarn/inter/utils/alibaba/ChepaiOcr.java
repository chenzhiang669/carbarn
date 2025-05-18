package com.carbarn.inter.utils.alibaba;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.qixiubao.update.LocalRunForbidenLog4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.binary.Base64.encodeBase64;



/**
 * 使用APPCODE进行云市场ocr服务接口调用
 */

public class ChepaiOcr {

    /*
     * 获取参数的json对象
     */
    public static JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String chepaiORC(String host,
                                 String path,
                                 String appcode,
                                 byte[] content){

        LocalRunForbidenLog4j.forbidenlog();

        Boolean is_old_format = false;//如果文档的输入中含有inputs字段，设置为True， 否则设置为False
        //请根据线上文档修改configure字段
        JSONObject configObj = new JSONObject();
        configObj.put("multi_crop", false);
        String config_str = configObj.toString();
        //            configObj.put("min_size", 5);
        //String config_str = "";

        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        Map<String, String> querys = new HashMap<String, String>();

        // 对图像进行base64编码
        String imgBase64 = new String(encodeBase64(content));
        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        try {
            if(is_old_format) {
                JSONObject obj = new JSONObject();
                obj.put("image", getParam(50, imgBase64));
                if(config_str.length() > 0) {
                    obj.put("configure", getParam(50, config_str));
                }
                JSONArray inputArray = new JSONArray();
                inputArray.add(obj);
                requestObj.put("inputs", inputArray);
            }else{
                requestObj.put("image", imgBase64);
                if(config_str.length() > 0) {
                    requestObj.put("configure", config_str);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String bodys = requestObj.toString();

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if(stat != 200){
                System.out.println("Http code: " + stat);
                System.out.println("http header error msg: "+ response.getFirstHeader("X-Ca-Error-Message"));
                System.out.println("Http body error msg:" + EntityUtils.toString(response.getEntity()));
                return null;
            }

            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            if(is_old_format) {
                JSONArray outputArray = res_obj.getJSONArray("outputs");
                String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
                JSONObject out = JSON.parseObject(output);
                System.out.println(out.toJSONString());
                return out.toJSONString();
            }else{
                System.out.println(res_obj.toJSONString());
                return res_obj.toJSONString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static byte[] readImageToByteArray(String imagePath) {
        File file = new File(imagePath);
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String host = "https://ocrcp.market.alicloudapi.com";
        String path = "/rest/160601/ocr/ocr_vehicle_plate.json";
        String appcode = "e537a32bb7de4e9a8f93186736184f48";
        String imgFile = "D:\\carbarn\\汽车车牌打码\\Noq6LNNX4N1746520887989.JPG";
        byte[] imageBytes = readImageToByteArray(imgFile);
        String result = chepaiORC(host, path, appcode, imageBytes);
        System.out.println(result);
        JSONObject json = JSON.parseObject(result);
        if(json.containsKey("plates")){
            JSONArray plates = json.getJSONArray("plates");
            for(int i = 0 ;i<plates.size();i++){
                JSONObject plate = plates.getJSONObject(i);
                if(plate.containsKey("roi")){
                    JSONObject roi = plate.getJSONObject("roi");
                    int w = (int)(roi.getInteger("w") * 1.5);
                    int h = (int)(roi.getInteger("h") * 1.3);
                    int x = roi.getInteger("x");
                    int y = roi.getInteger("y") - 10;

                    PlateMasking.masking(x,y,w,h,"ffffffff","JPG",imageBytes);
                }
            }
        }

    }
}