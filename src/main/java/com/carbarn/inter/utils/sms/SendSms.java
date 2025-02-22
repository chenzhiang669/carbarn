package com.carbarn.inter.utils.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.volcengine.model.request.*;
import com.volcengine.model.response.SmsCheckVerifyCodeResponse;
import com.volcengine.model.response.SmsSendResponse;
import com.volcengine.service.sms.SmsService;
import com.volcengine.service.sms.SmsServiceInfoConfig;
import com.volcengine.service.sms.impl.SmsServiceImpl;

import java.io.IOException;

public class SendSms {

    private static String AccessKeyId = "AKLTMDY3MGI0YzI0Zjg0NDdiODkzZDJjNGQ1ODZhZDMzZGE";
    private static String SecretAccessKey = "TXpnd056aGtOV0V3WmpBd05EWmtZV0ptTXpSa1pUQTJNRFUxT1RRNVpqUQ==";

    private static String smsAccount = "82089e98";

    private static String zh_templateId = "ST_8261c7cb";

    private static String other_language_templateId = "ST_826116c8";
    private static String sign = "卓雅科技";

    private static String scene = "注册验证码";

    public static void main(String[] args) throws IOException {
        sendVerifyCodeV2("18715026765", "zh");
//        checkVerifyCode("18715026765", "666228");
    }

    public static boolean sendVerifyCodeV2(String phoneNum, String language) {
        try {
            SmsService smsService = SmsServiceImpl.getInstance(new SmsServiceInfoConfig(AccessKeyId, SecretAccessKey));
            SmsSendVerifyCodeRequest req = new SmsSendVerifyCodeRequest();
            req.setPhoneNumber(phoneNum);
            req.setSmsAccount(smsAccount);
            if ("zh".equals(language)) {
                req.setTemplateId(zh_templateId);
            } else {
                req.setTemplateId(other_language_templateId);
            }
            req.setSign(sign);
            req.setExpireTime(600);
            req.setScene(scene);

            SmsSendResponse response = smsService.sendVerifyCodeV2(req);
            System.out.println(JSON.toJSONString(response));
            String code = response.getCode();
            if(code == null){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean checkVerifyCode(String phoneNum, String verifyCode) {
        try {
            SmsService smsService = SmsServiceImpl.getInstance(new SmsServiceInfoConfig(AccessKeyId, SecretAccessKey));
            SmsCheckVerifyCodeRequest req = new SmsCheckVerifyCodeRequest();

            req.setPhoneNumber(phoneNum);
            req.setSmsAccount(smsAccount);
            req.setCode(verifyCode);
            req.setScene(scene);


            SmsCheckVerifyCodeResponse response = smsService.checkVerifyCode(req);
            System.out.println(JSON.toJSONString(response));
            String result = response.getResult();
            if("0".equals(result)){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
