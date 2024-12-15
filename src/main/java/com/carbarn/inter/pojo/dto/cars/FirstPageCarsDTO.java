package com.carbarn.inter.pojo.dto.cars;

import lombok.Data;

@Data
public class FirstPageCarsDTO {
    private long id;
    private long user_id;
    private String name;
    private String plate_issue_date;
    private double mileage;
    private double price;
    private String upload_date;
    private String city;
    private String header_picture;
}
