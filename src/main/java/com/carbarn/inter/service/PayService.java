package com.carbarn.inter.service;

import com.carbarn.inter.pojo.pay.CreateGlobalOrderDTO;
import com.carbarn.inter.pojo.pay.CreateOrderDTO;
import com.carbarn.inter.pojo.pay.PayCallBackGlobalPOJO;
import com.carbarn.inter.pojo.pay.PayCallBackPOJO;
import com.carbarn.inter.utils.AjaxResult;

public interface PayService {

//    AjaxResult preorder();

    String callback(PayCallBackPOJO payCallBackDTO);

    AjaxResult createorder(CreateOrderDTO createOrderDTO);

    AjaxResult getOrderStatus(String reqsn);

    AjaxResult price();

    String callbackglobal(PayCallBackGlobalPOJO payCallBackGlobalPOJO);

    AjaxResult subVipPrice();

    AjaxResult getGlobalOrderStatus(String accessOrderId);
}
