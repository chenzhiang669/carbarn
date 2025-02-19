package com.carbarn.inter.utils.sms;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class VolcSms {

    public static String sendSms_url = "https://sms.volcengineapi.com?Action=SendSmsVerifyCode&Version=2020-01-01";
    public static String checkSms_url = "https://sms.volcengineapi.com?Action=CheckSmsVerifyCode&Version=2020-01-01";
    public static String SmsAccount = "";
    public static String Sign = "";
    public static String TemplateID = "";
    public static String Scene = "";

    public static int ExpireTime = 600;

    public static void SendSmsVerifyCode(String phoneNum) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(sendSms_url);
        httpPost.setHeader("Content-Type", "application/json");

        JSONObject jsonbody = new JSONObject();
        jsonbody.put("SmsAccount", SmsAccount);
        jsonbody.put("Sign", Sign);
        jsonbody.put("TemplateID", TemplateID);
        jsonbody.put("Scene", Scene);
        jsonbody.put("PhoneNumber", phoneNum);
        jsonbody.put("ExpireTime", ExpireTime);

        HttpEntity requestEntity = new StringEntity(
                jsonbody.toJSONString(),
                ContentType.APPLICATION_JSON
        );

        httpPost.setEntity(requestEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println(response);
    }

    public static void CheckSmsVerifyCode(String phoneNum, String code) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(checkSms_url);
        httpPost.setHeader("Content-Type", "application/json");

        JSONObject jsonbody = new JSONObject();
        jsonbody.put("SmsAccount", SmsAccount);
        jsonbody.put("Scene", Scene);
        jsonbody.put("PhoneNumber", phoneNum);
        jsonbody.put("Code", code);

        HttpEntity requestEntity = new StringEntity(
                jsonbody.toJSONString(),
                ContentType.APPLICATION_JSON
        );

        httpPost.setEntity(requestEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println(response);
    }

    public static void main(String[] args) {

    }
}
