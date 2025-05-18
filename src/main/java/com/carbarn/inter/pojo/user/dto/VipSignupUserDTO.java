package com.carbarn.inter.pojo.user.dto;

import lombok.Data;

@Data
public class VipSignupUserDTO {
    private long id; //用户id
    private String nickname; //昵称
    private String address; //车商地址
    private String car_dealership; //车行名称
    private String real_name; //用户真实名字
    private String front_of_id; //身份证正面(url)
    private String back_of_id; //身份证反面(url)
    private String province; //车商所在省份
    private String city; //车商所在城市
    private String invitation_code; //邀请码
    private String language = "zh";
}
