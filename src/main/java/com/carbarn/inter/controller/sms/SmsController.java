package com.carbarn.inter.controller.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.sms.SendSms;
import io.swagger.annotations.Api;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "短信服务")
@RestController
@RequestMapping("/carbarn/sms")
public class SmsController {

    @Autowired
    private ParamsMapper paramsMapper;

    @PostMapping("/sendVerifyCode")
    public AjaxResult sendVerifyCode(@RequestHeader(name = "language", required = true) String language,
                                     @RequestBody String body) {

        JSONObject json = JSON.parseObject(body);
        if (!json.containsKey("phone_num")) {
            return AjaxResult.error("Missing required parameter: 'phone_num'");
        }

        String phone_num = json.getString("phone_num");
        if (json.containsKey("area_code")) {
            String area_code = json.getString("area_code");
            phone_num = area_code + phone_num;
        }else if(!phone_num.startsWith("+")){
            phone_num = "+86" + phone_num;
        }

        String param_sms = paramsMapper.getValue(ParamKeys.param_sms);
        JSONObject sms_json = JSON.parseObject(param_sms);
        String AccessKeyId = sms_json.getString("AccessKeyId");
        String SecretAccessKey = sms_json.getString("SecretAccessKey");
        String smsAccount = sms_json.getString("smsAccount");
        String zh_templateId = sms_json.getString("zh_templateId");
        String other_language_templateId = sms_json.getString("other_language_templateId");
        String sign = sms_json.getString("sign");
        String global_sign = sms_json.getString("global_sign");
        int expireTime = sms_json.getInteger("expireTime");
        String scene = sms_json.getString("scene");

        boolean bool = SendSms.sendVerifyCodeV2(phone_num, language, AccessKeyId, SecretAccessKey, smsAccount,
                zh_templateId, other_language_templateId, sign, global_sign, expireTime, scene);
        if (bool) {
            return AjaxResult.success("发送验证码成功");
        } else {
            return AjaxResult.error("发送验证码失败");
        }
    }


//    @PostMapping("/checkVerifyCode")
//    public AjaxResult checkVerifyCode(@RequestHeader(name = "language", required = true) String language,
//                                     @RequestBody String body) {
//
//        JSONObject json = JSON.parseObject(body);
//        if(!json.containsKey("phone_num")){
//            return AjaxResult.error("Missing required parameter: 'phone_num'");
//        }else if(!json.containsKey("verify_code")){
//            return AjaxResult.error("Missing required parameter: 'verify_code'");
//        }
//
//        String phone_num = json.getString("phone_num");
//        String verify_code = json.getString("verify_code");
//
//        boolean bool = SendSms.checkVerifyCode(phone_num, verify_code);
//        if(bool){
//            return AjaxResult.success("验证码校验成功");
//        }else {
//            return AjaxResult.error("验证码校验失败");
//        }
//    }
}
