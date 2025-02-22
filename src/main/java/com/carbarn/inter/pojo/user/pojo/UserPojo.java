package com.carbarn.inter.pojo.user.pojo;

import lombok.Data;

@Data
public class UserPojo {
    private long id;
    private long user_count;
    private String phone_num;
    private String avatar;
    private String nickname;
    private String address;
    private String car_dealership;
    private String real_name;
    private String front_of_id;
    private String back_of_id;
    private String language;
    private int role;
    private String satoken;
}
