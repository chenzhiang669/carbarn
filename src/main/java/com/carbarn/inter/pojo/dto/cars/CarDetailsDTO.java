package com.carbarn.inter.pojo.dto.cars;

import lombok.Data;

import java.util.List;

@Data
public class CarDetailsDTO {
    private long id;
    private int brand_id;
    private String brand;
    private int series_id;
    private String series;
    private int type_id;
    private String type;
    private String name;
    private int user_id;
    private String header_picture;
    private String all_pictures;
    private double price;
    private double guide_price;
    private double floor_price;
    private String vin;
    private String proof;
    private int vehicleType;
    private String inspection_report;

    private List<CarInfosDTO> car_infos;
    private CarUserInfoDTO user_info;

}
