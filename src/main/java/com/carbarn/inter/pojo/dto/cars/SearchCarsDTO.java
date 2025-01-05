package com.carbarn.inter.pojo.dto.cars;

import lombok.Data;

@Data
public class SearchCarsDTO {
    private String language = null;

    private int pageNo = -1;
    private int pageSize = 10;

    private int pageStart = (pageNo - 1) * pageSize;

    private int brand_id = -1;

    private int series_id = -1;

    private int type_id = -1;

    private int type_of_car = -1; //车辆类型

    private String plate_issue_date = null; //上牌年份

    private double displacement = 0.0; //排量

    private String displacement_type = null; //排量类型

    private int color = -1; //颜色

    private int origin_country = -1; //国别

    private int transmission = -1; //变速箱

    private int type_of_manu = -1; //厂家类型

    private int emission_standards = -1; //排放标准

    private int seat_capacity = -1; //座位数量

    private int engine = -1; //能源类型

    private int drive_type = -1; //驱动方式

    private int box = -1; //车身结构

    private double car_age_up_limit = 0.0; //车龄上限

    private double car_age_low_limit = 0.0; //车龄下限

    private double price_up_limit = 0.0; //价格上限

    private double price_lower_limit = 0.0; //价格下限

    private double mileage_up_limit = 0.0; //里程上限

    private double mileage_lower_limit = 0.0; //里程下限

    private String order_field = "upload_date";

    private String order_way = "desc";

}
