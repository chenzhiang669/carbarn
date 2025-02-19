package com.carbarn.inter.utils.qixiubao;

//import cn.qixiubao.epc.EpcSign;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Qixiubao {

    public static final String url = "http://test-open-api2.0.nanxinwang.com//VinDecoder/decode";
    public static final String secret = "c60e8d3539b3748e882ed3beb4ac3f6c";
    public static final String appid = "7f5a424c687328576f337b0986dd14bd";
    public static List<JSONObject> http(Map<String, Object> map) throws IOException {

        List<JSONObject> carTypes = new ArrayList<JSONObject>();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for(String key:map.keySet()){
            builder.addPart(key,new StringBody(map.get(key).toString(), ContentType.create("text/plain", Consts.UTF_8)));
        }

        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);// 执行提交

        // 获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode != 200){
            return carTypes;
        }

        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        JSONObject responseJson = JSON.parseObject(responseString);
        if(!responseJson.containsKey("result")){
            return carTypes;
        }

        JSONObject resultJson = responseJson.getJSONObject("result");
        if(!resultJson.containsKey("models")){
            return carTypes;
        }

        JSONArray models = resultJson.getJSONArray("models");
        for(int i = 0; i < models.size(); i++){
            JSONObject model = parseModel(models.getJSONObject(i));
            carTypes.add(model);
        }


        return carTypes;
    }

    private static JSONObject parseModel(JSONObject json) {
        JSONObject data = new JSONObject();
        data.put("type", json.getOrDefault("model_name", null));
        data.put("series", json.getOrDefault("series", null));
        data.put("brand", json.getOrDefault("brand", null));
        data.put("year", json.getOrDefault("years", null));
        data.put("language", "zh");
        data.put("price", 0.0);
        data.put("floor_price", 0.0);
        data.put("color", "其他");
        data.put("power", 0.0);
        data.put("battery_capacity", 0.0);
        data.put("pure_electric_range", 0.0);


        try{
            if(json.containsKey("price")){
                String price = json.getString("price");
                if(price.contains("万")){
                    double guide_price = Double.valueOf(price.replace("万","")) * 10000;
                    data.put("guide_price", guide_price);  //TODO
                }
            }
        }catch (Exception e){
            data.put("guide_price", 0.0);
        }

        try{
            if(json.containsKey("engine")){
                String engine = json.getString("engine");
                if(engine.contains("T")){
                    data.put("displacement_type", "T");
                    double displacement = Double.valueOf(engine.replaceAll("T",""));
                    data.put("displacement", displacement);
                }else if(engine.contains("L")){
                    data.put("displacement_type", "L");
                    double displacement = Double.valueOf(engine.replaceAll("L",""));
                    data.put("displacement", displacement);
                }else{
                    data.put("displacement", 0.0);
                    data.put("displacement_type", "T");
                }

            }
        }catch (Exception e){
            data.put("displacement", 0.0);
            data.put("displacement_type", "T");
        }


        if(json.containsKey("level")){
            String level = Contants.type_of_cars.getOrDefault(json.getString("level"), "不限");
            data.put("type_of_car", level);
        }

        if(json.containsKey("trans_des")){
            String trans_des = json.getString("trans_des");
            data.put("transmission", trans_des);  //TODO
        }

        if(json.containsKey("emission_standard")){
            String emission_standard = Contants.emission_standards.getOrDefault(json.getString("emission_standard"), "其他");
            data.put("emission_standards", emission_standard);
        }

        data.put("type_of_manu", "其他");
//        if(json.containsKey("mum_type")){
//            String capacity = json.getString("mum_type");
//            data.put("type_of_manu", label);
//        }

        if(json.containsKey("fuel_type")){
            String fuel_type = Contants.emission_standards.getOrDefault(json.getString("fuel_type"), "其他");
            data.put("engine", fuel_type);
        }

        if(json.containsKey("driver_type")){
            String driver_type = json.getString("driver_type");
            if(driver_type.contains("前驱")){
                data.put("drive_type", "前驱");
            }else if(driver_type.contains("后驱")){
                data.put("drive_type", "后驱");
            }else if(driver_type.contains("四驱")){
                data.put("drive_type", "四驱");
            }else{
                data.put("drive_type", "未知");
            }

        }

        if(json.containsKey("body")){
            String body = json.getString("body");
            if(body.contains("单厢")){
                data.put("box", "单厢");
            }else if(body.contains("两厢")){
                data.put("box", "两厢");
            }else if(body.contains("三厢")){
                data.put("box", "三厢");
            }else{
                data.put("box", "未知");
            }
        }

        return data;
    }

    public static List<JSONObject> searchVin(String vin) {


        Map<String, Object> parameters = new HashMap<>();
        parameters.put("secret", secret);
        parameters.put("appid", appid);
        parameters.put("version", "4.6.0");
        parameters.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        parameters.put("vin", vin);
        EpcSign epcSign = new EpcSign();
        String sign = "";
        try {

            sign = epcSign.sign(parameters);
            System.out.println(sign);
            parameters.put("sign", sign);
            List<JSONObject> result = http(parameters);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(String.valueOf(System.currentTimeMillis() / 1000));
//        List<JSONObject> result = searchVin("LVHFC1652M6044846");
//        for(JSONObject r:result){
//            System.out.println(r.toJSONString());
//        }


        System.out.println(searchVin("LBIWG3E10P8196847"));
    }
}
