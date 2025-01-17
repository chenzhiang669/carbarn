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
    private String manufacture_date; //出厂日期

    @Override
    public String toString() {
        return "FirstPageCarsDTO{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                ", plate_issue_date='" + plate_issue_date + '\'' +
                ", mileage=" + mileage +
                ", price=" + price +
                ", upload_date='" + upload_date + '\'' +
                ", city='" + city + '\'' +
                ", header_picture='" + header_picture + '\'' +
                ", manufacture_date='" + manufacture_date + '\'' +
                '}';
    }
}
