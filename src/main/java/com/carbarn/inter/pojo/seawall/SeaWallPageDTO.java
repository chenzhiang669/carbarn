package com.carbarn.inter.pojo.seawall;

import lombok.Data;

@Data
public class SeaWallPageDTO {
    private int pageNo = -1;
    private int pageSize = 10;

    private int pageStart = (pageNo - 1) * pageSize;

    private int seaWallType = -1;

    private int is_first_page = 0;

    private String keywords = null;
}
