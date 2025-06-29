package com.carbarn.inter.pojo.seawall;

import lombok.Data;

@Data
public class SeaWallPOJO {
    private int id;

    private String title;

    private int seaWallType = -1;

    private int is_first_page = 0;

    private String picture;

    private String url;

    private String create_time;
}
