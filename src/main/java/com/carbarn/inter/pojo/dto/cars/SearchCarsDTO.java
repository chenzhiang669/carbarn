package com.carbarn.inter.pojo.dto.cars;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchCarsDTO {
    private String language = null;

    private int pageNo = -1;
    private int pageSize = 10;

    private int pageStart = (pageNo - 1) * pageSize;

    private int brand_id = -1;

    private int series_id = -1;

    private int type_id = -1;

    private List<Integer> type_of_car = new ArrayList<Integer>(); //车辆类型

    private String plate_issue_date = null; //上牌年份

    private double displacement = 0.0; //排量

    private String displacement_type = null; //排量类型

    private List<Integer> color = new ArrayList<Integer>(); //颜色

    private List<Integer> origin_country = new ArrayList<Integer>(); //国别

    private List<Integer> transmission = new ArrayList<Integer>(); //变速箱

    private List<Integer> type_of_manu = new ArrayList<Integer>(); //厂家类型

    private List<Integer> emission_standards = new ArrayList<Integer>(); //排放标准

    private int seat_capacity = -1; //座位数量

    private String province = null;

    private String city = null;

    private List<Integer> engine = new ArrayList<Integer>(); //能源类型

    private List<Integer> drive_type = new ArrayList<Integer>(); //驱动方式

    private List<Integer> box = new ArrayList<Integer>(); //车身结构

    private double car_age_up_limit = 0.0; //车龄上限

    private double car_age_low_limit = 0.0; //车龄下限

    private double price_up_limit = 0.0; //价格上限

    private double price_lower_limit = 0.0; //价格下限

    private double mileage_up_limit = 0.0; //里程上限

    private double mileage_lower_limit = 0.0; //里程下限

    private String keywords; //关键词搜索字段
    private String order_field = "upload_date";

    private String order_way = "desc";
}
