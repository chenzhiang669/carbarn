package com.carbarn.contract.pojo;

import lombok.Data;

@Data
public class ContractPOJO {
    private String contract_id;
    private int car_id;
    private long buyer_id;
    private String buyer_nickname;
    private int buyer_state;
    private double buyer_guarantee_fund;
    private String buyer_phone_num;
    private int seller_id;
    private int seller_state;
    private double seller_guarantee_fund;
    private String buyer_address;
    private String buyer_email;
    private double price;
    private int cnt;
    private String other_agreements;
    private String buyer_confirm_time;
    private String seller_confirm_time;
    private String create_time;
    private String operationContract;
    private String userContract;
    private String pay_note;
    private int operate = -1; //操作类型: 保存 / 确认
}
