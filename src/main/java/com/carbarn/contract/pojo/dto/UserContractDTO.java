package com.carbarn.contract.pojo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserContractDTO {

    private String contract_id;

    private long buyer_id;

    private int buyer_state;

    private double buyer_guarantee_fund;

    private long seller_id;

    private int seller_state;

    private double seller_guarantee_fund;

    private String create_time;

    private double price;

    private int cnt;

    private int vehicleType;

    private String header_picture;

    private String brand;

    private String series;

    private String type;


}
