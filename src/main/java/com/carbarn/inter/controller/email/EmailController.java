package com.carbarn.inter.controller.email;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.pojo.sms.CheckVerifyCodeDTO;
import com.carbarn.inter.service.EmailService;
import com.carbarn.inter.service.SmsService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.sms.SendSms;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "邮件验证码服务")
@RestController
@RequestMapping("/carbarn/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendVerifyCode")
    public AjaxResult sendVerifyCode(@RequestHeader(name = "language", required = true) String language,
                                     @RequestBody String body) {

        JSONObject json = JSON.parseObject(body);
        if (!json.containsKey("email")) {
            return AjaxResult.error("Missing required parameter: 'email'");
        }

        String email = json.getString("email");

        return emailService.sendVerifyCode(email);
    }


    @PostMapping("/checkVerifyCode")
    public AjaxResult checkVerifyCode(@RequestHeader(name = "language", required = true) String language,
                                     @RequestBody CheckVerifyCodeDTO checkVerifyCodeDTO) {


        if(checkVerifyCodeDTO.getEmail() == null
                || checkVerifyCodeDTO.getVeri_code() == null){
            return AjaxResult.error("Missing required parameter: 'email' or 'veri_code'");
        }

        boolean bool = emailService.checkVerifyCode(checkVerifyCodeDTO);
        if(bool){
            return AjaxResult.success("Verification code validation succeeded");
        }else{
            return AjaxResult.error("Incorrect verification code");
        }

    }
}
