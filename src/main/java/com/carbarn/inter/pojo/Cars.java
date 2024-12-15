package com.carbarn.inter.pojo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class Cars {
    private long id;
    private long user_id;
    private String brand;
    private String name;
    private String color;
    private double mileage;
    private String vin;
    private String plate_issue_date;
    private double price;
    private String displacement;
    private String engine;
    private String transmission;
    private String emission_standards;
    private String type_of_manu;
    private String type_of_car;
    private double new_car_price;
    private String ctali_date;
    private String avi_date;
    private String city;
    private int is_deal;
    private String upload_date;
    private String update_date;
    private String description;
    private String header_picture;
    private String all_pictures;
    private String vrc;
}
