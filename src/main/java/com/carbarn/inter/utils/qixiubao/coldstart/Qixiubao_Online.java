package com.carbarn.inter.utils.qixiubao.coldstart;

//import cn.qixiubao.epc.EpcSign;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.qixiubao.EpcSign;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.PSource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Qixiubao_Online {

    public static final String brand_url = "http://open-api2.0.nanxinwang.com/Bsm/brand";
    public static final String series_url = "http://open-api2.0.nanxinwang.com/Bsm/series";
    public static final String type_url = "http://open-api2.0.nanxinwang.com//Bsm/majorModel";
    public static final String type_detail_url = "http://open-api2.0.nanxinwang.com//Vehicle/getModelKeyValue";
    public static final String secret = "533f15bd38980184203ada7b0fe029de";
    public static final String appid = "663f8c5be37ec1db7c6b38704c7a1c28";
    public static final String version = "1.0.1";
    public static final String version_type_detail = "1.0.0";

    public static String http(Map<String, Object> map, String url) throws IOException {

        List<JSONObject> carTypes = new ArrayList<JSONObject>();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for (String key : map.keySet()) {
            builder.addPart(key, new StringBody(map.get(key).toString(), ContentType.create("text/plain", Consts.UTF_8)));
        }

        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);// 执行提交

        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }


//    public static String proxy(String brand_id, String series_id, String page){
    public static String proxy(String type_id){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("secret",secret);
        parameters.put("appid", appid);
        parameters.put("version", version_type_detail);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        System.out.println(timestamp);
        parameters.put("timestamp", timestamp);
        parameters.put("model_id", type_id);
//        parameters.put("brand_id", brand_id);
//        parameters.put("series_id", series_id);
//        parameters.put("is_added", "0");
//        parameters.put("is_modified", "0");
//        parameters.put("page", page);
        EpcSign epcSign = new EpcSign();
        String sign = "";
        try {

            sign = epcSign.sign(parameters);
            System.out.println(sign);
            parameters.put("sign", sign);
            String response = http(parameters, type_detail_url);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void count_car_type(){
        File file = new File("D:\\carbarn\\汽修宝\\");
        String[] files = file.list();
        for (String filename :files){
            System.out.println(filename);
        }
    }

    public static void main(String[] args) throws IOException {

//        Logger wireLogger = (Logger) LoggerFactory.getLogger("org.apache.http.wire");
//        wireLogger.setLevel(Level.OFF); // 禁用日志输出
//
//        Logger headersLogger = (Logger) LoggerFactory.getLogger("org.apache.http.headers");
//        headersLogger.setLevel(Level.OFF); // 禁用日志输出
//        Logger headersimpl = (Logger) LoggerFactory.getLogger("org.apache.http.impl");
//        headersimpl.setLevel(Level.OFF); // 禁用日志输出
//        Logger headersclient = (Logger) LoggerFactory.getLogger("org.apache.http.client");
//        headersclient.setLevel(Level.OFF); // 禁用日志输出
////        String response = proxy("15", "65", "3");
////        System.out.println(response);
//        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\series_original_data")));
//        String line = null;
//        int count = 0;
//        while((line = br.readLine()) != null){
//            count = count + 1;
//            if(count > 16){
//                break;
//            }
//            JSONObject infos = JSON.parseObject(line.trim());
//            JSONArray result = infos.getJSONArray("result");
//            BufferedWriter wr = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\汽修宝\\type_original_data" + count)));
//            for(int i = 0; i < result.size(); i++){
//                JSONObject brands_info = result.getJSONObject(i);
//                String series_id = brands_info.getString("id");
//                String brand_id = brands_info.getString("brand_id");
//                String name = brands_info.getString("name");
//                System.out.println(brand_id + "\t" + series_id + "\t" + name);
//                for(int j = 1; j < 50; j++){
//                    String response = proxy(brand_id, series_id, String.valueOf(j));
//                    JSONObject response_json = JSON.parseObject(response);
//                    if(response_json.containsKey("result")){
//                        JSONObject result_json = response_json.getJSONObject("result");
//                        System.out.println(result_json.getJSONArray("list").size());
//                        if(result_json.containsKey("list") && result_json.getJSONArray("list").size() <= 0){
//                            break;
//                        }else{
//                            System.out.println(response);
//                            wr.write(response + "\n");
//                        }
//                    }
//                }
//            }
//
//            wr.flush();
//            wr.close();
//        }





    }
}
