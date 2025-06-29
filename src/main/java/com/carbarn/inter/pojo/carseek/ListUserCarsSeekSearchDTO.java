package com.carbarn.inter.pojo.carseek;

import lombok.Data;

@Data
public class ListUserCarsSeekSearchDTO {
    private long user_id;
    private int pageNo = -1;
    private int pageSize = 10;

    private int pageStart = (pageNo - 1) * pageSize;
    private String language;
    private String today;
}
