package com.carbarn.inter.pojo.user.pojo;

import lombok.Data;

@Data
public class UserPojo {
    private long id;
    private long parent_id;
    private long user_count;
    private String phone_num;
    private String email;
    private String avatar;
    private String nickname;
    private String address;
    private String car_dealership;
    private String real_name;
    private String front_of_id;
    private String back_of_id;
    private String province; //车商所在省份
    private String province_name;
    private String city; //车商所在城市
    private String city_name;
    private String invitation_code; //邀请码
    private String language;
    private int role;
    private String satoken;
    private String expire_time;
}
