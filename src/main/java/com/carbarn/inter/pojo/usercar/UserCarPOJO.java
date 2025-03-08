package com.carbarn.inter.pojo.usercar;

import lombok.Data;

@Data
public class UserCarPOJO {
    private long id;
    private String brand;
    private String series;
    private String type;
    private String manufacture_date;
    private double mileage;
    private double price;
    private String update_date;
    private String reason;
    private int state;
}
