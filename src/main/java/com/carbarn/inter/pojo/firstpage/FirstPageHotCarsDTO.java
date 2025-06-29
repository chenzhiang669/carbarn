package com.carbarn.inter.pojo.firstpage;

import lombok.Data;

@Data
public class FirstPageHotCarsDTO {
    private String id;
    private String brand;
    private String series;
    private String type;
    private String name;
    private String header_picture;
    private double price;
    private double mileage;
    private int viewed_count;
}
