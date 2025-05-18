package com.carbarn.inter.utils.qixiubao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.qixiubao.update.JDBCUtils;
import com.carbarn.inter.utils.qixiubao.update.LocalRunForbidenLog4j;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.carbarn.inter.utils.qixiubao.Contants.*;

public class QixiubaoHttp {

    private static final Logger logger = LoggerFactory.getLogger(QixiubaoHttp.class);

    //通用http查询接口
    public static String http(Map<String, Object> params,
                              String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for (String key : params.keySet()) {
            builder.addPart(key, new StringBody(params.get(key).toString(), ContentType.create("text/plain", Consts.UTF_8)));
        }

        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);// 执行提交

        // 获取响应状态码
//        int statusCode = response.getStatusLine().getStatusCode();
//        if(statusCode != 200){
//            return null;
//        }
//
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        return responseString;
    }

    //查询所有品牌
    public static String searchAllBrands() {
        try {
            EpcSign epcSign = new EpcSign();
            String sign = "";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("secret", secret);
            params.put("appid", appid);
            params.put("version", version);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("timestamp", timestamp);
            sign = epcSign.sign(params);
            params.put("sign", sign);
            String response = http(params, brand_url);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    //某个品牌的所有车系查询
    public static String searchSeries(String brand_id) {
        try {
            EpcSign epcSign = new EpcSign();
            String sign = "";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("secret", secret);
            params.put("appid", appid);
            params.put("version", version);
            params.put("brand_id", brand_id);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("timestamp", timestamp);
            sign = epcSign.sign(params);
            params.put("sign", sign);
            String response = http(params, series_url);
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    //某个品牌-车系的所有车型查询
    public static List<String> searcTypes(String brand_id,
                                          String series_id) {
        List<String> typeLists = new ArrayList<String>();
        EpcSign epcSign = new EpcSign();
        String sign = "";


        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("secret", secret);
            params.put("appid", appid);
            params.put("version", version);
            params.put("brand_id", brand_id);
            params.put("series_id", series_id);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("timestamp", timestamp);
            params.put("page", "1");
            sign = epcSign.sign(params);
            params.put("sign", sign);
            String response = http(params, type_url);
            typeLists.add(response);

            JSONObject responseJson = JSON.parseObject(response);
            int page_count = 0;
            if (responseJson.containsKey("result")) {
                JSONObject result = responseJson.getJSONObject("result");
                if (result.containsKey("page_count")) {
                    page_count = result.getInteger("page_count");
                }
            }

            logger.info("there are " + page_count + " page_counts to be spider");


            for (int page = 2; page <= page_count; page++) {

                params = new HashMap<String, Object>();
                params.put("secret", secret);
                params.put("appid", appid);
                params.put("version", version);
                params.put("brand_id", brand_id);
                params.put("series_id", series_id);
                timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                params.put("timestamp", timestamp);
                params.put("page", String.valueOf(page));
                sign = epcSign.sign(params);
                params.put("sign", sign);
                response = http(params, type_url);
                typeLists.add(response);
                logger.info("spider page " + page + " successfully");
            }

            return typeLists;

        } catch (Exception e) {
            e.printStackTrace();
            return typeLists;
        }
    }


    //增量车型查询
    public static List<String> searcIncrementTypes() {
        List<String> typeLists = new ArrayList<String>();
        EpcSign epcSign = new EpcSign();
        String sign = "";


        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("secret", secret);
            params.put("appid", appid);
            params.put("version", version);
            params.put("is_added", "1");
            params.put("is_modified", "1");
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("timestamp", timestamp);
            params.put("page", "1");
            sign = epcSign.sign(params);
            params.put("sign", sign);
            String response = http(params, type_url);
            typeLists.add(response);

            JSONObject responseJson = JSON.parseObject(response);
            int page_count = 0;
            if (responseJson.containsKey("result")) {
                JSONObject result = responseJson.getJSONObject("result");
                if (result.containsKey("page_count")) {
                    page_count = result.getInteger("page_count");
                }
            }

            logger.info("there are " + page_count + " page_counts to be spider");


            for (int page = 2; page <= page_count; page++) {

                params = new HashMap<String, Object>();
                params.put("secret", secret);
                params.put("appid", appid);
                params.put("version", version);
                params.put("is_added", "1");
                params.put("is_modified", "1");
                timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                params.put("timestamp", timestamp);
                params.put("page", String.valueOf(page));
                sign = epcSign.sign(params);
                params.put("sign", sign);
                response = http(params, type_url);
                typeLists.add(response);
                logger.info("spider page " + page + " successfully");
            }

            return typeLists;

        } catch (Exception e) {
            e.printStackTrace();
            return typeLists;
        }
    }

    //查询某个车型的详细信息
    public static JSONObject searchTypeDetails(String type_id) {
        try {
            EpcSign epcSign = new EpcSign();
            String sign = "";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("secret", secret);
            params.put("appid", appid);
            params.put("version", version_type_detail);
            params.put("model_id", type_id);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("timestamp", timestamp);
            sign = epcSign.sign(params);
            params.put("sign", sign);
            String response = http(params, type_detail_url);
            JSONObject details = Transform.typeDetailsTransform(response, type_id);
            return details;
        } catch (Exception e) {
            return null;
        }
    }

    //获取车架号所属汽车信息
    public static String searchVin(String vin) {

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("secret", Contants.secret);
            parameters.put("appid", Contants.appid);
            parameters.put("version", Contants.vin_version);
            parameters.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
            parameters.put("vin", vin);
            EpcSign epcSign = new EpcSign();
            String sign = "";
            sign = epcSign.sign(parameters);
            parameters.put("sign", sign);
            String responseString = http(parameters, vin_url);
            return responseString;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //搜索车型概要
    public static String searchSummary() {
        try {
            EpcSign epcSign = new EpcSign();
            String sign = "";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("secret", secret);
            params.put("appid", appid);
            params.put("version", version);
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("timestamp", timestamp);
            sign = epcSign.sign(params);
            params.put("sign", sign);
            String response = http(params, summary_url);
            return response;
        } catch (Exception e) {
            return null;
        }
    }


    public static void main(String[] args) {
        LocalRunForbidenLog4j.forbidenlog();
//        JSONObject str = searchTypeDetails("72103");
//        System.out.println(str);
//        String str = searchSeries("402");
        List<String> types = searcTypes("181","3298");
        types.forEach(x -> {
            System.out.println(x);
        });
//        List<String> types = searcIncrementTypes();

//
//        for (String type : types) {
//            JSONObject jsonObject = JSON.parseObject(type.trim());
//            if (jsonObject.containsKey("result")) {
//                JSONObject result = jsonObject.getJSONObject("result");
//                JSONArray list = result.getJSONArray("list");
//
//                for (int i = 0; i < list.size(); i++) {
//                    JSONObject data = list.getJSONObject(i);
//                    if (data.getInteger("id") == 72103) {
//                        System.out.println(data);
//                    }
//                }
//            }
//        }
//
//        String vinInfo = searchVin("LB1WG3E68S8246513");
//        System.out.println(vinInfo);
//        String summary = searchSummary();
//        System.out.println(summary);
    }
}
