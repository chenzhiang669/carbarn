package com.carbarn.inter.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.pojo.pay.CreateGlobalOrderDTO;
import com.carbarn.inter.pojo.pay.CreateOrderDTO;
import com.carbarn.inter.pojo.pay.PayCallBackGlobalPOJO;
import com.carbarn.inter.pojo.pay.PayCallBackPOJO;
import com.carbarn.inter.service.PayService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.tonglian.globalization.RSAUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.TreeMap;

@Api(tags = "支付服务")
@RestController
@RequestMapping("/carbarn/pay")
public class PayController {

    private final Logger logger = LoggerFactory.getLogger(PayController.class);
    @Autowired
    private PayService payService;

    @Autowired
    private ParamsMapper paramsMapper;

//    @PostMapping("/preorder")
//    public AjaxResult preorder() {
//        return payService.preorder();
//    }


    @PostMapping("/createorder")
    public AjaxResult createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        if (createOrderDTO.getUser_id() == -1) {
            return AjaxResult.error("Missing required parameter 'user_id'");
        } else if (createOrderDTO.getOrder_type() == null || "".equals(createOrderDTO.getOrder_type())) {
            return AjaxResult.error("Missing required parameter 'order_type'");
        }


        return payService.createorder(createOrderDTO);

//        String order_type = createOrderDTO.getOrder_type();
//        if("global_contract_guarantee_fund".equals(order_type)){


//        else if(createOrderDTO.getPay_type() == null){
//            return AjaxResult.error("Missing required parameter 'pay_type'");
//        }else if(!"W06".equals(createOrderDTO.getPay_type())  && !"A02".equals(createOrderDTO.getPay_type())){
//            return AjaxResult.error("parameter 'pay_type' should be [W06:微信] or [A02:支付宝]");
//        } else {
//            return payService.createorder(createOrderDTO);
//        }

    }

    @PostMapping("/orderstatus")
    public AjaxResult orderstatus(@RequestBody String body) {
        JSONObject json = JSON.parseObject(body);
        if (!json.containsKey("reqsn")) {
            return AjaxResult.error("Missing required parameter 'reqsn'");
        } else {
            String reqsn = json.getString("reqsn");
            return payService.getOrderStatus(reqsn);
        }

    }


    @PostMapping("/globalOrderStatus")
    public AjaxResult globalOrderStatus(@RequestBody String body) {
        JSONObject json = JSON.parseObject(body);
        if (!json.containsKey("accessOrderId")) {
            return AjaxResult.error("Missing required parameter 'accessOrderId'");
        } else {
            String accessOrderId = json.getString("accessOrderId");
            return payService.getGlobalOrderStatus(accessOrderId);
        }

    }


    @PostMapping("/price")
    public AjaxResult price() {
        return payService.price();
    }

    //子账户会员注册金额
    @PostMapping("/subVipPrice")
    public AjaxResult subVipPrice() {
        return payService.subVipPrice();
    }

    //国内支付异步调用接口
    @PostMapping(value = "/callback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String callback(PayCallBackPOJO payCallBackDTO) {
        return payService.callback(payCallBackDTO);
    }


    //国外支付异步调用接口
    @PostMapping(value = "/callbackglobal", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String callbackglobal(@RequestParam MultiValueMap<String, String> formData) {

        TreeMap<String, String> params = new TreeMap<String, String>();
        for (String key : formData.keySet()) {
            params.put(key, formData.getFirst(key));
        }

        String jsonString = JSON.toJSONString(params);
        logger.info(jsonString);

        String param_payment_global = paramsMapper.getValue(ParamKeys.param_payment_global);
        JSONObject param_payment_global_json = JSON.parseObject(param_payment_global);
        String pub_key = param_payment_global_json.getString("pub_key");
        logger.info("pub_key: {}", pub_key);
        //使用系统公钥: 验证签名是否合法
        boolean bool = RSAUtil.verifySign(jsonString, "RSA2", pub_key);
        if (bool) {
//            PayCallBackGlobalPOJO payCallBackGlobalPOJO = JSON.toJavaObject(JSON.parseObject(jsonString), PayCallBackGlobalPOJO.class);
//            return payService.callbackglobal(payCallBackGlobalPOJO);
            logger.info("国际支付回调函数参数RSA2签名合法");
        } else {
//            return "FAIL";
            logger.info("国际支付回调函数参数RSA2签名不合法");
        }


        PayCallBackGlobalPOJO payCallBackGlobalPOJO = JSON.toJavaObject(JSON.parseObject(jsonString), PayCallBackGlobalPOJO.class);
        return payService.callbackglobal(payCallBackGlobalPOJO);
    }

}
