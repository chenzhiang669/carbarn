package com.carbarn.inter.pojo.usercar;

import lombok.Data;

@Data
public class UserLikeCarSearchDTO {
    private String language = null;

    private long user_id = -1;

    private int pageNo = -1;
    private int pageSize = 10;

    private int pageStart = (pageNo - 1) * pageSize;

    private String keywords;
}
