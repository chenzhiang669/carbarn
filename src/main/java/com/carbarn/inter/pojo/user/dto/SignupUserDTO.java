package com.carbarn.inter.pojo.user.dto;

import lombok.Data;

@Data
public class SignupUserDTO {
    private String phone_num; //电话号码
    private String veri_code; //验证码
    private long user_count = 3423;
    private String avatar; //头像
    private String nickname; //昵称
}
