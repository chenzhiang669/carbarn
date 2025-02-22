package com.carbarn.inter.pojo.user.pojo;

import lombok.Data;

@Data
public class UserViewCountPojo {
    private long user_id;
    private String dt;
    private int view_count;
    private int viewed_count;
    private int contact_count;
    private int contacted_count;
}
