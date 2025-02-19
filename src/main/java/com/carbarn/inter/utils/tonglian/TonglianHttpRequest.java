package com.carbarn.inter.utils.tonglian;

import com.alibaba.fastjson.JSON;
import com.carbarn.inter.pojo.pay.OrderPOJO;
import com.carbarn.inter.utils.Utils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TonglianHttpRequest {
    public static String url = "https://vsp.allinpay.com/apiweb/tranx/query";

    public static String getOrderStatus(OrderPOJO orderPOJO) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));


        TreeMap<String, String> treemap = new TreeMap<String, String>();
        treemap.put("cusid", orderPOJO.getCusid());
        treemap.put("appid", orderPOJO.getAppid());
        treemap.put("version", orderPOJO.getVersion());
        treemap.put("reqsn", orderPOJO.getReqsn());

        treemap.put("randomstr", orderPOJO.getRandomstr());
        treemap.put("signtype", orderPOJO.getSigntype());
        String sign = SybUtil.unionSign(treemap,orderPOJO.getAppkey(), orderPOJO.getSigntype());
        treemap.put("sign", sign);

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for(Map.Entry<String, String> entry:treemap.entrySet()){
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
        httpPost.setEntity(formEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Status Code: " + statusCode);

        // 获取响应体
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity);
        System.out.println("Response Body: " + responseString);
        return responseString;
    }

    public static void main(String[] args) throws IOException {
        OrderPOJO orderPOJO = new OrderPOJO();

        orderPOJO.setCusid("564361055214UTH");
        orderPOJO.setAppid("00339478");
        orderPOJO.setValidtime("12");
        orderPOJO.setReqsn("20250215120641pjsFyl3k5K");
        orderPOJO.setRandomstr("b3zTuPITyJ");
        orderPOJO.setSign("MD5");
        orderPOJO.setAppkey("allinpay1");
        System.out.println(getOrderStatus(orderPOJO));
    }
}
