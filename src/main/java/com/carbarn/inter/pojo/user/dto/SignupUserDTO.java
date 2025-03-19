package com.carbarn.inter.pojo.user.dto;

import com.carbarn.inter.pojo.language.LanguageConstant;
import lombok.Data;

@Data
public class SignupUserDTO {
    private String phone_num; //电话号码
    private String veri_code; //验证码
    private long user_count = 3423;
    private String avatar = "http://image.chechuhai.top/carbarn/files/default_avatar/uVxyH4HqcP1740213823406.webp"; //头像
    private String nickname; //昵称
    private String language = LanguageConstant.ZH;
}
