package com.carbarn.contract.pojo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class UserContractDTO {

    private String contract_id;

    private long buyer_id;

    private int buyer_state;

    private String buyer_nickname;

    private double buyer_guarantee_fund;

    private long seller_id;

    private int seller_state;

    private String seller_nickname;

    private double seller_guarantee_fund;

    private String create_time;

    private double price;

    private int cnt;

    private int vehicleType;

    private String header_picture;

    private String brand;

    private String series;

    private String type;

    private String operationContract;
    private String userContract;
    private String pay_note;
    private long operation_first_review_time;
    private long operation_second_review_time;
    private List<Object> payment_information;
    private String remaining_days;
    private int delete_flag;


}
