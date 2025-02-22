package com.carbarn.inter.controller.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.sms.SendSms;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(tags = "短信服务")
@RestController
@RequestMapping("/carbarn/sms")
public class SmsController {

    @PostMapping("/sendVerifyCode")
    public AjaxResult sendVerifyCode(@RequestHeader(name = "language", required = true) String language,
                                     @RequestBody String body) {

        JSONObject json = JSON.parseObject(body);
        if(!json.containsKey("phone_num")){
            return AjaxResult.error("Missing required parameter: 'phone_num'");
        }

        String phone_num = json.getString("phone_num");

        boolean bool = SendSms.sendVerifyCodeV2(phone_num, language);
        if(bool){
            return AjaxResult.success("发送验证码成功");
        }else {
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
