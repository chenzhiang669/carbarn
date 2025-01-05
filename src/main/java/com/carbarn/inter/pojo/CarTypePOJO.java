package com.carbarn.inter.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CarTypePOJO {
    private	String	type;
    private	int	type_id;
    private	int	series_id;
    private	int	brand_id;
    private	String	year;
    private	String	language;
    private	double	price = 0.0;
    private	double	floor_price = 0.0;
    private	double	guide_price = 0.0;
    private	double	displacement = 0.0;
    private String displacement_type = "T";
    private	double	power = 0.0;
    private	double	battery_capacity = 0.0;
    private	double	pure_electric_range = 0.0;
    private	int	color = -1;
    private	int	transmission = -1;
    private	int	emission_standards = -1;
    private	int	type_of_manu = -1;
    private	int	engine = -1;
    private	int	drive_type = -1;
    private	int	box = -1;
    private	int	type_of_car = -1;


    public static CarTypePOJO getNewCarTypePojo(String message) {

        String[] infos = message.split(" ",4);
        String brand = infos[0];
        String series = infos[1];
        String year = infos[2].replaceAll("款","");
        String type = infos[2] + " " + infos[3];
        String language = "zh";

        CarTypePOJO carTypePOJO = new CarTypePOJO();
        carTypePOJO.setType(type);
        carTypePOJO.setYear(year);
        carTypePOJO.setLanguage(language);
        carTypePOJO.setPrice(0.0);
        carTypePOJO.setFloor_price(0.0);
        carTypePOJO.setGuide_price(0.0);
        carTypePOJO.setDisplacement(0.0);
        carTypePOJO.setDisplacement_type("T");
        carTypePOJO.setPower(0.0);
        carTypePOJO.setBattery_capacity(0.0);
        carTypePOJO.setPure_electric_range(0);
        carTypePOJO.setColor(-1);
        carTypePOJO.setTransmission(-1);
        carTypePOJO.setEmission_standards(-1);
        carTypePOJO.setType_of_manu(-1);
        carTypePOJO.setEngine(-1);
        carTypePOJO.setDrive_type(-1);
        carTypePOJO.setBox(-1);
        carTypePOJO.setType_of_car(-1);

        return carTypePOJO;
    }


    public static List<String> mapping_fields(){
        List<String> mapping_fields = new ArrayList<String>();

        mapping_fields.add("color");

        return mapping_fields;
    }

    @Override
    public String toString() {
        return "CarTypePOJO{" +
                "type='" + type + '\'' +
                ", type_id=" + type_id +
                ", series_id=" + series_id +
                ", brand_id=" + brand_id +
                ", year='" + year + '\'' +
                ", language='" + language + '\'' +
                ", price=" + price +
                ", floor_price=" + floor_price +
                ", guide_price=" + guide_price +
                ", displacement=" + displacement +
                ", displacement_type='" + displacement_type + '\'' +
                ", power=" + power +
                ", battery_capacity=" + battery_capacity +
                ", pure_electric_range=" + pure_electric_range +
                ", color=" + color +
                ", transmission=" + transmission +
                ", emission_standards=" + emission_standards +
                ", type_of_manu=" + type_of_manu +
                ", engine=" + engine +
                ", drive_type=" + drive_type +
                ", box=" + box +
                ", type_of_car=" + type_of_car +
                '}';
    }
}
