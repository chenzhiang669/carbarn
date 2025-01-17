package com.carbarn.inter.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.pojo.CarTypePOJO;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JinyutangHttp {

    public static String url_base = "https://inner-api.jytche.com/vin/parses?vin=%s&car_code=null&channel=mayi";
    public static String url_base_sign = "70add6664591637631b6efe2ac314eb7";

    public static String detail_url_base = "https://inner-api.jytche.com/vin/liYang/%s";
    public static String detail_url_base_sign = "70add6664591637631b6efe2ac314eb7";

    public static String appid = "trends67482fa5f11c4";
    public static String access_token = "96cee3573a17b1eff3136b36c146dc2b";

    public static List<JSONObject> searchVin(String vin){
        List<JSONObject> details = new ArrayList<JSONObject>();
        String urlString = String.format(url_base, vin);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(urlString);
            request.setHeader("appId", appid);
            request.setHeader("sign", url_base_sign);
            request.setHeader("Access-Token", access_token);
            request.setHeader("Accept-Encoding","gzip,deflate,br");
            request.setHeader("Accept-Language","zh-CN,zh;q=0.9");
            CloseableHttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

            Object json = JSON.parse(responseBody);
            if(json instanceof JSONArray){
                JSONArray jsonarray = (JSONArray) json;

                for(int i = 0; i < jsonarray.size(); i++){
                    String level_id = jsonarray.getJSONObject(i).getString("level_id");
                    JSONObject detail = JinyutangHttp.searchDetails(level_id);
                    if(detail != null){
                        details.add(detail);
                    }
                }
                return details;
            }else if(json instanceof JSONObject){
                return null;
            }

        } catch (IOException e) {
            return null;
        }
        return null;
    }


    public static JSONObject searchDetails(String level_id){
        String urlString = String.format(detail_url_base, level_id);

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(urlString);
            request.setHeader("appId", appid);
            request.setHeader("sign", detail_url_base_sign);
            request.setHeader("Access-Token", access_token);
            request.setHeader("Accept-Encoding","gzip,deflate,br");
            request.setHeader("Accept-Language","zh-CN,zh;q=0.9");
            CloseableHttpResponse response = httpClient.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");

            JSONObject detail = JinyutangHttp.createData(responseBody);
            return detail;

        } catch (IOException e) {
            return null;
        }
    }

    private static JSONObject createData(String responseBody){
        try{
            CarTypePOJO carTypePOJO = new CarTypePOJO();
            JSONObject data = new JSONObject();
            JSONObject json = JSON.parseObject(responseBody);


            data.put("type", json.getOrDefault("model_name", null));
            data.put("series", json.getOrDefault("series_name", null));
            data.put("brand", json.getOrDefault("brand_name", null));
            data.put("year", json.getOrDefault("model_year", null));
            data.put("year", json.getOrDefault("model_year", null));
            data.put("language", "zh");
            data.put("price", 0.0);
            data.put("floor_price", 0.0);
            data.put("guide_price", 0.0);


            try{
                if(json.containsKey("capacity")){
                    JSONObject capacity = json.getJSONObject("capacity");
                    if(capacity.containsKey("label")){
                        String label =  capacity.getString("label");
                        if(label.contains("T")){
                            data.put("displacement_type", "T");
                        }else if(label.contains("L")){
                            data.put("displacement_type", "T");
                        }
                        double displacement = Double.valueOf(label.replaceAll("[TL]",""));
                        data.put("displacement", displacement);
                    }
                }
            }catch (Exception e){
                data.put("displacement", 0.0);
                data.put("displacement_type", "T");
            }

            data.put("power", 0.0);
            data.put("battery_capacity", 0.0);
            data.put("pure_electric_range", 0.0);
            if(json.containsKey("gearbox")){
                JSONObject capacity = json.getJSONObject("gearbox");
                if(capacity.containsKey("label")){
                    String label =  capacity.getString("label");
                    data.put("transmission", label);
                }
            }

            if(json.containsKey("emission")){
                JSONObject capacity = json.getJSONObject("emission");
                if(capacity.containsKey("label")){
                    String label =  capacity.getString("label");
                    data.put("emission_standards", label);
                }
            }


            if(json.containsKey("mum_type")){
                JSONObject capacity = json.getJSONObject("mum_type");
                if(capacity.containsKey("label")){
                    String label =  capacity.getString("label");
                    data.put("type_of_manu", label);
                }
            }

            if(json.containsKey("fuel_type")){
                JSONObject capacity = json.getJSONObject("fuel_type");
                if(capacity.containsKey("label")){
                    String label =  capacity.getString("label");
                    data.put("engine", label);
                }
            }

            if(json.containsKey("drive_mode")){
                JSONObject capacity = json.getJSONObject("drive_mode");
                if(capacity.containsKey("label")){
                    String label =  capacity.getString("label");
                    data.put("drive_type", label);
                }
            }

            if(json.containsKey("body_type")){
                JSONObject capacity = json.getJSONObject("body_type");
                if(capacity.containsKey("label")){
                    String label =  capacity.getString("label");
                    data.put("box", label);
                }
            }


            data.put("type_of_car", json.getOrDefault("vehicle_type", null));
            data.put("color", null);

            return data;
        }catch (Exception e){
            return null;
        }
    }

    public static void main(String[] args) {
        List<JSONObject> details = JinyutangHttp.searchVin("LVHFC1668G6037011");
        System.out.println(details);
//        double displacement = Double.valueOf("1.5L".replaceAll("[TL]",""));
//        System.out.println(displacement);
    }

}
