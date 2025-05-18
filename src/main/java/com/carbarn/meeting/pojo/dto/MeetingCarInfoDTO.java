package com.carbarn.meeting.pojo.dto;

import com.carbarn.contract.pojo.dto.ContractCarInfoDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class MeetingCarInfoDTO {
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

    private double price;

    public static MeetingCarInfoDTO getMeetingCarInfoDTO(Map<String, Object> carsInfo) {
        MeetingCarInfoDTO meetingCarInfoDTO = new MeetingCarInfoDTO();
        meetingCarInfoDTO.setCar_id((Integer) carsInfo.get("id"));
        meetingCarInfoDTO.setName((String) carsInfo.get("name"));
        meetingCarInfoDTO.setManufacture_date((String) carsInfo.get("manufacture_date"));
        meetingCarInfoDTO.setVin((String) carsInfo.getOrDefault("vin", null));
        meetingCarInfoDTO.setMileage(Double.valueOf(carsInfo.getOrDefault("mileage", 0.0).toString()));
        meetingCarInfoDTO.setColor((String) carsInfo.getOrDefault("color_name", null));
        meetingCarInfoDTO.setBrand((String) carsInfo.getOrDefault("brand", null));
        meetingCarInfoDTO.setSeries((String) carsInfo.getOrDefault("series", null));
        meetingCarInfoDTO.setType((String) carsInfo.getOrDefault("type", null));
        meetingCarInfoDTO.setVehicleType((Integer) carsInfo.getOrDefault("vehicleType", 0));
        meetingCarInfoDTO.setHeader_picture((String) carsInfo.getOrDefault("header_picture", null));
        meetingCarInfoDTO.setPrice(Double.valueOf(carsInfo.getOrDefault("price", 0.0).toString()));
        return meetingCarInfoDTO;
    }
}
