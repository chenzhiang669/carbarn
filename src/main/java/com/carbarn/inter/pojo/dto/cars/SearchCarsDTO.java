package com.carbarn.inter.pojo.dto.cars;

import lombok.Data;

@Data
public class SearchCarsDTO {
    private String brand;

    private String color;

    private double price_up_limit;

    private double price_lower_limit;

    private double mileage_up_limit;

    private double mileage_lower_limit;
}
