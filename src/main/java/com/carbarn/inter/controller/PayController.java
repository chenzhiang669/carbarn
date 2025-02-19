package com.carbarn.inter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.pojo.pay.CreateOrderDTO;
import com.carbarn.inter.pojo.pay.PayCallBackPOJO;
import com.carbarn.inter.service.PayService;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carbarn/pay")
public class PayController {

    @Autowired
    private PayService payService;

//    @PostMapping("/preorder")
//    public AjaxResult preorder() {
//        return payService.preorder();
//    }


    @PostMapping("/createorder")
    public AjaxResult createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        if(createOrderDTO.getUser_id() == -1){
            return AjaxResult.error("Missing required parameter 'user_id'");
        }else if(createOrderDTO.getPay_type() == null){
            return AjaxResult.error("Missing required parameter 'pay_type'");
        }else if(!"W06".equals(createOrderDTO.getPay_type())  && !"A02".equals(createOrderDTO.getPay_type())){
            return AjaxResult.error("parameter 'pay_type' should be [W06:微信] or [A02:支付宝]");
        } else if(createOrderDTO.getOrder_type() == null || "".equals(createOrderDTO.getOrder_type())){
            return AjaxResult.error("Missing required parameter 'order_type'");
        }else {
            return payService.createorder(createOrderDTO);
        }

    }

    @PostMapping("/orderstatus")
    public AjaxResult orderstatus(@RequestBody String body) {
        JSONObject json = JSON.parseObject(body);
        if(!json.containsKey("reqsn")){
            return AjaxResult.error("Missing required parameter 'reqsn'");
        }else {
            String reqsn = json.getString("reqsn");
            return payService.getOrderStatus(reqsn);
        }

    }


    // 登录接口
    @PostMapping(value = "/callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String callback(PayCallBackPOJO payCallBackDTO) {
        return payService.callback(payCallBackDTO);
    }


}
