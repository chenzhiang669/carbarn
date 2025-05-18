package com.carbarn.contract.pojo.dto;

import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ContractCarInfoDTO {

    private int car_id;

    private String name;

    private String manufacture_date;

    private String vin;

    private double mileage;

    private String color;

    private String brand;

    private String series;

    private String type;

    private int vehicleType;

    private String header_picture;

    private List<Integer> car_condition;

    private List<Integer> coating;

    private List<Integer> component;

    private List<Integer> engine_condition;

    private List<Integer> transmission_condition;

    private List<Integer> mileage_contition;

    private List<Integer> number_of_transfers;

    public static ContractCarInfoDTO getContractCarInfoDTO(Map<String, Object> carsInfo){
        ContractCarInfoDTO contractCarInfoDTO = new ContractCarInfoDTO();
        contractCarInfoDTO.setCar_id((Integer) carsInfo.get("id"));
        contractCarInfoDTO.setName((String) carsInfo.get("name"));
        contractCarInfoDTO.setManufacture_date((String) carsInfo.get("manufacture_date"));
        contractCarInfoDTO.setVin((String) carsInfo.getOrDefault("vin",null));
        contractCarInfoDTO.setMileage(Double.valueOf(carsInfo.getOrDefault("mileage", 0.0).toString()) );
        contractCarInfoDTO.setColor((String) carsInfo.getOrDefault("color_name",null));
        contractCarInfoDTO.setBrand((String) carsInfo.getOrDefault("brand",null));
        contractCarInfoDTO.setSeries((String) carsInfo.getOrDefault("series",null));
        contractCarInfoDTO.setType((String) carsInfo.getOrDefault("type",null));
        contractCarInfoDTO.setVehicleType((Integer) carsInfo.getOrDefault("vehicleType",0));
        contractCarInfoDTO.setHeader_picture((String) carsInfo.getOrDefault("header_picture",null));
        contractCarInfoDTO.setCar_condition((List<Integer>) carsInfo.getOrDefault("car_condition",new ArrayList<Integer>()));
        contractCarInfoDTO.setCoating((List<Integer>) carsInfo.getOrDefault("coating",new ArrayList<Integer>()));
        contractCarInfoDTO.setComponent((List<Integer>) carsInfo.getOrDefault("component",new ArrayList<Integer>()));
        contractCarInfoDTO.setEngine_condition((List<Integer>) carsInfo.getOrDefault("engine_condition",new ArrayList<Integer>()));
        contractCarInfoDTO.setTransmission_condition((List<Integer>) carsInfo.getOrDefault("transmission_condition",new ArrayList<Integer>()));
        contractCarInfoDTO.setMileage_contition((List<Integer>) carsInfo.getOrDefault("mileage_contition",new ArrayList<Integer>()));
        contractCarInfoDTO.setNumber_of_transfers((List<Integer>) carsInfo.getOrDefault("number_of_transfers",new ArrayList<Integer>()));
        return contractCarInfoDTO;
    }

}
