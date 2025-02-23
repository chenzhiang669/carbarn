package com.carbarn.inter.utils.hzyh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.pojo.pay.PreOrderPojo;
import com.carbarn.inter.utils.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HangZhouYinHangUtils {

    public static String hzyh_url = "";
    public static String callback_url = "http://47.122.70.102:8080/carbarn/pay/callback";

    public static String txnAmt = "2800000";

    public static String generateTxnOrderId(){
        LocalDateTime localDateTime = LocalDateTime.now();

        String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomChar = Utils.getRandomChar(10);
        return time + randomChar;
    }
    public static PreOrderPojo getPreOrderPojo(){
        PreOrderPojo preOrderPojo = new PreOrderPojo();
        preOrderPojo.setUser_id(1);
        preOrderPojo.setEncoding("UTF-8");
        preOrderPojo.setSdkAppId("201701");
        preOrderPojo.setCertId("17010100001");
        preOrderPojo.setSignAture("XXXXXX"); //TODO
        preOrderPojo.setTxnType("1000");
        preOrderPojo.setTxnSubType("100000");
        preOrderPojo.setAesWay("03"); //TODO
        preOrderPojo.setMerId("XXXX"); //TODO
        preOrderPojo.setMerName("XXXXX");//TODO
        preOrderPojo.setSecMerId("XXXXX"); //TODO
        preOrderPojo.setBackEndUrl(callback_url);
        preOrderPojo.setFrontEndUrl("");
        String TxnOrderId = generateTxnOrderId();
        preOrderPojo.setTxnOrderId(TxnOrderId); //todo
        preOrderPojo.setTxnOrderTime(TxnOrderId.substring(0,14)); //todo
        preOrderPojo.setTxnOrderBody(""); //todo
        preOrderPojo.setTxnAmt(txnAmt);
        preOrderPojo.setTxnCcyType("156");
        return preOrderPojo;
    }


    public static PreOrderPojo http(){
        PreOrderPojo preOrderPojo = getPreOrderPojo();
        preOrderPojo.setTokenCode(Utils.getRandomChar(50));
        preOrderPojo.setTokenEnd(Utils.getRandomChar(6));
        preOrderPojo.setTokenType(Utils.getRandomChar(6));
        preOrderPojo.setTokenBegin(Utils.getRandomChar(6));
        preOrderPojo.setRespTxnSsn(Utils.getRandomChar(6));
        preOrderPojo.setRespTxnTime(Utils.getRandomChar(6));
        return preOrderPojo;
//        try {
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpPost request = new HttpPost(hzyh_url);
//            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
//            request.setEntity(entity);
//
//            CloseableHttpResponse response = httpClient.execute(request);
//            HttpEntity responseEntity = response.getEntity();
//            if (responseEntity != null) {
//                JSONObject result_json = (JSONObject) JSON.parse(EntityUtils.toString(responseEntity));
//                String respCode = result_json.getString("respCode");
//                if("0000".equals(respCode)){
//                    System.out.println(result_json);
//                    JSONObject txnTokenPayData = result_json.getJSONObject("txnTokenPayData");
//                    preOrderPojo.setTokenCode(txnTokenPayData.getString("tokenCode"));
//                    preOrderPojo.setTokenEnd(txnTokenPayData.getString("tokenEnd"));
//                    preOrderPojo.setTokenType(txnTokenPayData.getString("tokenType"));
//                    preOrderPojo.setTokenBegin(txnTokenPayData.getString("tokenBegin"));
//                    preOrderPojo.setRespTxnSsn(result_json.getString("respTxnSsn"));
//                    preOrderPojo.setRespTxnTime(result_json.getString("respTxnTime"));
//                    preOrderPojo.setRespMsg(result_json.getString("respMsg"));
//                    return preOrderPojo;
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return null;
    }

    public static void main(String[] args) {
        System.out.println(generateTxnOrderId().substring(0,14));
    }

}
