package com.carbarn.inter.service;


import com.carbarn.inter.pojo.sms.CheckVerifyCodeDTO;
import com.carbarn.inter.utils.AjaxResult;

public interface SmsService {

    boolean checkVerifyCode(CheckVerifyCodeDTO checkVerifyCodeDTO);
}
