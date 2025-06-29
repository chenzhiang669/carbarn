package com.carbarn.inter.utils.tonglian.globalization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.qixiubao.update.LocalRunForbidenLog4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Test {
//    public static String tonglian_url = "https://cnp-test.allinpay.com/gateway/cnp/quickpay";
    public static String tonglian_url = "https://cnp.allinpay.com/gateway/cnp/quickpay";
    public static String base_http_url = "http://chechuhai.top:8080/static/global-pay.html?redirect_url=";

    public static void main(String[] args) {

        LocalRunForbidenLog4j.forbidenlog();

        String param = "{\n" +
                "        \"shippingCity\": \"Mountain View\",\n" +
                "        \"sign\": \"M9SXrc5URabffYqLdbrSOQ9Xwo1EDKMfPH+9Ylz+Mu+cpsF4AKzycmz+e/UQQcdML8Hz8Kip27mU4gpoUeUlHmVfKEbVW2VU14C6S9QxoZNZR2WAZTttL3xhkxhwTiOfLGlvQ0YRDbhFSq+995QE1pom9ERht0rmJjQTEmXpXUwtFlYBrc4Pfu3kk21nwHKt0hulu2WoeQ/fd0GVjP3TPnNUOXl0pMBeV6dTByC5tHXBz2ZtDn0yWPXPCjnFwwgQIU0g9Aa95tU3LOCx0CiwbCh2Z0JT8oLsDa9VDHzM8Yw7Z0+uuOnBTS8lEfrtOoColYBBCmzFhEx2JxuqXTRAvA==\",\n" +
                "        \"language\": \"en\",\n" +
                "        \"shippingAddress1\": \"1295 Charleston Rd\",\n" +
                "        \"billingAddress1\": \"1295 Charleston Rd\",\n" +
                "        \"shippingCountry\": \"US\",\n" +
                "        \"shippingZipCode\": \"94043\",\n" +
                "        \"billingFirstName\": \"noreal\",\n" +
                "        \"billingCountry\": \"US\",\n" +
                "        \"signType\": \"RSA2\",\n" +
                "        \"currency\": \"USD\",\n" +
                "        \"returnUrl\": \"https://chechuhai.top/car-static/global-payed.html\",\n" +
                "        \"email\": \"1461251592@qq.com\",\n" +
                "        \"amount\": \"0.01\",\n" +
                "        \"shippingLastName\": \"name\",\n" +
                "        \"accessOrderId\": \"20250614200151n4rSLSK9wI\",\n" +
                "        \"billingZipCode\": \"94043\",\n" +
                "        \"shippingPhone\": \"00000000000\",\n" +
                "        \"billingState\": \"CA\",\n" +
                "        \"version\": \"V2.0.0\",\n" +
                "        \"productInfo\": \"[]\",\n" +
                "        \"billingLastName\": \"name\",\n" +
                "        \"transType\": \"Pay\",\n" +
                "        \"shippingFirstName\": \"noreal\",\n" +
                "        \"mchtId\": \"086310072990001\",\n" +
                "        \"notifyUrl\": \"https://chechuhai.top/carbarn/pay/callbackglobal\",\n" +
                "        \"shippingState\": \"CA\",\n" +
                "        \"billingCity\": \"Mountain View\",\n" +
                "        \"billingPhone\": \"00000000000\"\n" +
                "    }";

        JSONObject param_json = JSON.parseObject(param);
        System.out.println(param_json.toJSONString());

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(tonglian_url);

            // 准备表单参数
            List<NameValuePair> params = new ArrayList<>();
            for (String key : param_json.keySet()) {
                params.add(new BasicNameValuePair(key, param_json.getString(key)));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(params));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            System.out.println("code: " + code);
            if(200 == response.getStatusLine().getStatusCode()){
//                String entity = response.getEntity().toString();
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println(responseString);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                JSONObject json = JSON.parseObject(responseString);
                String payUrl = json.getString("payUrl");
                System.out.println(payUrl);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                String payUrlEncode = URLEncoder.encode(payUrl, StandardCharsets.UTF_8.toString());
                System.out.println(base_http_url +  payUrlEncode);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
