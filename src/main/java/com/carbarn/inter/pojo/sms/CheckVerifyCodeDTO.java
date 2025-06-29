package com.carbarn.inter.pojo.sms;

import lombok.Data;

@Data
public class CheckVerifyCodeDTO {
    private String phone_num; //电话号码
    private String area_code; //区号
    private String veri_code; //验证码
    private String email; //邮件
}
