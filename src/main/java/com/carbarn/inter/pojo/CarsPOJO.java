package com.carbarn.inter.pojo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CarsPOJO {
    private int	id;
    private int	car_id;
    private int	user_id;
    private int	brand_id;
    private int	series_id;
    private int	type_id;
    private int	color = -1;
    private int	engine = -1;
    private int	transmission = -1;
    private int	emission_standards = -1;
    private int	type_of_manu = -1;
    private int	type_of_car = -1;
    private int	origin_country = -1;
    private int	drive_type = -1;
    private int	box = -1;
    private double	displacement = 0.0;
    private String	displacement_type = "T";
    private int	seat_capacity = 0;

    private double new_car_price = 0.0;
    private String	ctali_date;
    private String	avi_date;
    private double	car_age = 0.0;
    private double	mileage = 0.0;
    private String	vin;

    private String manufacture_date;
    private String	plate_issue_date;
    private double	price = 0.0;
    private double	floor_price = 0.0;
    private double	guide_price = 0.0;
    private double	power = 0.0;
    private double	battery_capacity = 0.0;
    private double	pure_electric_range = 0.0;

    private String province;
    private String	city;
    private int	is_deal = 0;
    private int	is_lock = 0;
    private int	state = 0;
    private Date upload_date;
    private Date update_date;
    private String	description;
    private String	header_picture;
    private String	all_pictures;
    private String	proof;
    private List<Integer> label = new ArrayList<Integer>();

    private String label_string = "-1";
    private String	inspection_report;
    private int	car_condition = -1;
    private int	coating = -1;
    private int	component = -1;
    private int	engine_condition = -1;
    private int	transmission_condition = -1;
    private int	number_of_transfers = -1;
    private int	mileage_contition = -1;
    private String	car_condition_desc;

    @Override
    public String toString() {
        return "CarsPOJO{" +
                "id=" + id +
                ", car_id=" + car_id +
                ", user_id=" + user_id +
                ", brand_id=" + brand_id +
                ", series_id=" + series_id +
                ", type_id=" + type_id +
                ", color=" + color +
                ", engine=" + engine +
                ", transmission=" + transmission +
                ", emission_standards=" + emission_standards +
                ", type_of_manu=" + type_of_manu +
                ", type_of_car=" + type_of_car +
                ", origin_country=" + origin_country +
                ", drive_type=" + drive_type +
                ", box=" + box +
                ", displacement=" + displacement +
                ", displacement_type='" + displacement_type + '\'' +
                ", seat_capacity=" + seat_capacity +
                ", ctali_date='" + ctali_date + '\'' +
                ", avi_date='" + avi_date + '\'' +
                ", car_age=" + car_age +
                ", mileage=" + mileage +
                ", vin='" + vin + '\'' +
                ", manufacture_date='" + manufacture_date + '\'' +
                ", plate_issue_date='" + plate_issue_date + '\'' +
                ", price=" + price +
                ", floor_price=" + floor_price +
                ", guide_price=" + guide_price +
                ", power=" + power +
                ", battery_capacity=" + battery_capacity +
                ", pure_electric_range=" + pure_electric_range +
                ", city='" + city + '\'' +
                ", is_deal=" + is_deal +
                ", is_lock=" + is_lock +
                ", state=" + state +
                ", upload_date=" + upload_date +
                ", update_date=" + update_date +
                ", description='" + description + '\'' +
                ", header_picture='" + header_picture + '\'' +
                ", all_pictures='" + all_pictures + '\'' +
                ", proof='" + proof + '\'' +
                ", label='" + label + '\'' +
                ", inspection_report='" + inspection_report + '\'' +
                ", car_condition=" + car_condition +
                ", coating=" + coating +
                ", component=" + component +
                ", engine_condition=" + engine_condition +
                ", transmission_condition=" + transmission_condition +
                ", number_of_transfers=" + number_of_transfers +
                ", mileage_contition=" + mileage_contition +
                ", car_condition_desc='" + car_condition_desc + '\'' +
                '}';
    }
}
