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

    private List<Integer> car_condition = new ArrayList<Integer>();
    private List<Integer> coating = new ArrayList<Integer>();
    private List<Integer> component = new ArrayList<Integer>();
    private List<Integer> engine_condition = new ArrayList<Integer>();
    private List<Integer> transmission_condition = new ArrayList<Integer>();
    private List<Integer> number_of_transfers = new ArrayList<Integer>();
    private List<Integer> mileage_contition = new ArrayList<Integer>();

    private String label_string = "-1";
    private String	inspection_report;
    private String	car_condition_string = "-1";
    private String	coating_string = "-1";
    private String	component_string = "-1";
    private String	engine_condition_string = "-1";
    private String	transmission_condition_string = "-1";
    private String	number_of_transfers_string = "-1";
    private String	mileage_contition_string = "-1";
    private String	car_condition_desc;

}
