package com.carbarn.inter.pojo.carseek;

import lombok.Data;

@Data
public class CarsSeekPOJO {
    private long id;
    private long user_id;
    private long brand_id;
    private long series_id;
    private long type_id;
    private String manufacture_date_lower;
    private String manufacture_date_upper;
    private double price_upper;
    private double price_lower;
    private double mileage_upper;
    private double mileage_lower;
    private int color;
    private int cnt;
    private String country_code;
    private String expire_time;
    private String arrival_port;
    private String description;
    private String randomString;
}
