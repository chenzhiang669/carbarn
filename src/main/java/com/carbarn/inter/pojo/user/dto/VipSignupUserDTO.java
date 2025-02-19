package com.carbarn.inter.pojo.user.dto;

import lombok.Data;

@Data
public class VipSignupUserDTO {
    private long id; //用户id
    private String address; //车商地址
    private String car_dealership; //车行名称
    private String real_name; //用户真实名字
    private String front_of_id; //身份证正面(url)
    private String back_of_id; //身份证反面(url)
    private String language = "zh";
}
