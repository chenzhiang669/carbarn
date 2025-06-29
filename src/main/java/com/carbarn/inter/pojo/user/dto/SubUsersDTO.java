package com.carbarn.inter.pojo.user.dto;

import lombok.Data;

@Data
public class SubUsersDTO {
    private long id;
    private long parent_id;
    private String phone_num;
    private String area_code;
    private String avatar;
    private String nickname;
    private int role;
    private String expire_time; //会员到期时间
    private int cars_count_on_sale; //在售汽车数量
    private int cars_count_on_delist; //下架汽车数量
}
