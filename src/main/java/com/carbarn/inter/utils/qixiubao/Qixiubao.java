//package com.carbarn.inter.utils.qixiubao;
//
//import cn.qixiubao.epc.EpcSign;
//import org.apache.http.Consts;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Qixiubao {
//
//    public static final String url = "http://test-open-api2.0.nanxinwang.com//VinDecoder/decode";
//    public static final String secret = "c60e8d3539b3748e882ed3beb4ac3f6c";
//    public static final String appid = "7f5a424c687328576f337b0986dd14bd";
//    public static void http(Map<String, Object> map) throws IOException {
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(url);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//        for(String key:map.keySet()){
//            builder.addPart(key,new StringBody(map.get(key).toString(), ContentType.create("text/plain", Consts.UTF_8)));
//        }
//
//        HttpEntity entity = builder.build();
//        httpPost.setEntity(entity);
//        HttpResponse response = httpClient.execute(httpPost);// 执行提交
//
//        // 获取响应状态码
//        int statusCode = response.getStatusLine().getStatusCode();
//        System.out.println("Status Code: " + statusCode);
//
//        // 获取响应体
//        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
//        System.out.println("Response: " + responseString);
//
//
//    }
//
//    public static void main(String[] args) throws IOException {
//
//
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("secret", secret);
//        parameters.put("appid", appid);
//        parameters.put("version", "4.6.0");
//        parameters.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
//        parameters.put("vin", "LFV3A24G6C3049890");
//        EpcSign epcSign = new EpcSign();
//        String sign = "";
//        try {
//
//            sign = epcSign.sign(parameters);
//
//        } catch (Exception e) {
//
//            // TODO Auto-generated catch block
//
//            e.printStackTrace();
//
//        }
//
//        System.out.println(sign);
//
//        parameters.put("sign", sign);
//
//        http(parameters);
//
//    }
//}
