package com.carbarn.inter.pojo.seawall;

import lombok.Data;

@Data
public class SeaWallPOJO {
    private int id;

    private String title;

    private int seaWallType = -1;

    private String picture;

    private String url;

    private String create_time;
}
