package com.carbarn.inter.service;


import com.carbarn.inter.pojo.sms.CheckVerifyCodeDTO;
import com.carbarn.inter.utils.AjaxResult;

public interface EmailService {

    AjaxResult sendVerifyCode(String email);

    boolean checkVerifyCode(CheckVerifyCodeDTO checkVerifyCodeDTO);
}
