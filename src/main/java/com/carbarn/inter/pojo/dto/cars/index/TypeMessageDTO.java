package com.carbarn.inter.pojo.dto.cars.index;

import lombok.Data;

@Data
public class TypeMessageDTO {
    private String brand = null;

    private String series = null;

    private String type = null;

    private int brand_id;

    private int series_id;

    private int type_id;

}
