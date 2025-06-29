package com.carbarn.contract.contants;

import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.List;

public class ContractState {
    //买家状态
    public static int buyer_state_draft = 0; //草稿箱状态
    public static int buyer_state_wating_seller_confirm = 1; //等待卖家确认状态(支付保障金)
    public static int buyer_state_be_returned = 2; //被退回状态
    public static int buyer_state_seller_confirm = 3; //卖家已确认
    public static int buyer_state_platform_review = 4;//平台审核
    public static int buyer_state_platform_wating_paycar = 5;//待支付车款
    public static int buyer_state_platform_upload_contract = 6;//合同待上传
    public static int buyer_state_platform_review_again = 7;//合同复审中
    public static int buyer_state_inspection_car = 8;//验车
    public static int buyer_state_platform_wating_transport = 9;//车辆运输中
    public static int buyer_state_car_landed = 10;//到岸
    public static int buyer_state_customs_clearance = 11; //清关
    public static int buyer_state_car_delivery = 12; //交付
    public static int buyer_state_finished = 13; //交易完成

    public static Integer[] buyer_states = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13};

    //----------------------------------------------分隔线-----------------------------------------------

    //卖家状态
    public static int seller_state_draft = 0; //草稿箱
    public static int seller_state_new_contract = 1; //新合同
    public static int seller_state_return = 2; //已退回
    public static int seller_state_modified = 3; //有修改
    public static int seller_state_waiting_buyer_confirm = 4; //等待买家确认(支付保障金)
    public static int seller_state_waiting_buyer_paycar = 5; //等待买家支付车款(尾款)
    public static int seller_state_waiting_deliver_car = 6; //车辆待交付
    public static int seller_state_finished = 7; //完成
    public static Integer[] seller_states = new Integer[]{0,1,2,3,4,5,6,7};
}
