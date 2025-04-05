
package com.carbarn.inter.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.contract.mapper.ContractMapper;
import com.carbarn.contract.pojo.ContractPOJO;
import com.carbarn.contract.service.ContractService;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.mapper.PayMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.pay.CreateOrderDTO;
import com.carbarn.inter.pojo.pay.OrderPOJO;
import com.carbarn.inter.pojo.pay.PayCallBackPOJO;
import com.carbarn.inter.service.PayService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.tonglian.SybUtil;
import com.carbarn.inter.utils.tonglian.TonglianHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private PayMapper payMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ParamsMapper paramsMapper;

    private static String callback_success_code = "0000";
    private static String callback_unpay = "2000";

//    @Override
//    public AjaxResult preorder() {
//        PreOrderPojo preOrderPojo = HangZhouYinHangUtils.http();
//        if(preOrderPojo == null){
//            return AjaxResult.error("构建预订单失败");
//        }
//        payMapper.insertNewPreOrder(preOrderPojo);
//
//        Map<String, String> result = new HashMap<String, String>();
//        result.put("tokenCode", preOrderPojo.getTokenCode());
//        result.put("txnAmt", preOrderPojo.getTxnAmt());
//        result.put("tokenCode", preOrderPojo.getTokenCode());
//
//        return AjaxResult.success("构建预订单成功", result);
//
//    }

    @Override
    public AjaxResult createorder(CreateOrderDTO createOrderDTO) {
        switch (createOrderDTO.getOrder_type()){
            case "vip_sign":
                return createVipSignOrder(createOrderDTO);
            case "contract_guarantee_fund":
                return createContractGuaranteeFundOrder(createOrderDTO);
            default:
                return AjaxResult.error("order_type is unvalid");
        }


    }

    //创建合同保障金支付订单
    private AjaxResult createContractGuaranteeFundOrder(CreateOrderDTO createOrderDTO) {
        int user_id = createOrderDTO.getUser_id();
        String contract_id = createOrderDTO.getContract_id();
        if(contract_id == null || "".equals(contract_id)){
            return AjaxResult.error("param [contract_id] is unvalid");
        }

        String param_contract = paramsMapper.getValue(ParamKeys.param_payment);  //从参数库表中获取支付相关信息
        OrderPOJO orderPOJO = JSONObject.toJavaObject(JSON.parseObject(param_contract), OrderPOJO.class);
//        OrderPOJO orderPOJO = payMapper.getDefaultOrderInfo();
        orderPOJO.setUser_id(createOrderDTO.getUser_id());
        orderPOJO.setPaytype(createOrderDTO.getPay_type());
        orderPOJO.setOrder_type(createOrderDTO.getOrder_type());
        orderPOJO.setContract_id(createOrderDTO.getContract_id());
        orderPOJO.setBody("车出海车辆交易保障金");

        ContractPOJO contractPOJO = contractMapper.getContractInfo(contract_id);
        if(contractPOJO == null){
            return AjaxResult.error("param [contract_id] is unvalid");
        }

        long buyer_id = contractPOJO.getBuyer_id();
        long seller_id = contractPOJO.getSeller_id();
        if(buyer_id == user_id){
            double buyer_guarantee_fund = contractPOJO.getBuyer_guarantee_fund();
            String trxamt = String.valueOf((int)(buyer_guarantee_fund * 100));
            orderPOJO.setTrxamt(trxamt);
        }else if(seller_id == user_id){
            double seller_guarantee_fund = (int)contractPOJO.getSeller_guarantee_fund();
            String trxamt = String.valueOf((int)(seller_guarantee_fund * 100));
            orderPOJO.setTrxamt(trxamt);
        }

        TreeMap<String, String> treemap = new TreeMap<String, String>();
        treemap.put("cusid", orderPOJO.getCusid());
        treemap.put("appid", orderPOJO.getAppid());
        treemap.put("version", orderPOJO.getVersion());
        treemap.put("trxamt", orderPOJO.getTrxamt());

        String reqsn = generateTxnOrderId();
        treemap.put("reqsn", reqsn);
        orderPOJO.setReqsn(reqsn);
        orderPOJO.setExpiretime("");

        treemap.put("notify_url", orderPOJO.getNotify_url());
        treemap.put("validtime", orderPOJO.getValidtime());
        treemap.put("body", orderPOJO.getBody());
        treemap.put("limit_pay", orderPOJO.getLimit_pay());

        String randomstr = Utils.getRandomChar(10);
        treemap.put("randomstr", randomstr);
        orderPOJO.setRandomstr(randomstr);

        treemap.put("paytype", orderPOJO.getPaytype());
        treemap.put("signtype", orderPOJO.getSigntype());
        treemap.put("isdirectpay", orderPOJO.getIsdirectpay());


        String sign = SybUtil.unionSign(treemap, orderPOJO.getAppkey(), orderPOJO.getSigntype());
        if(sign == null){
            return AjaxResult.error("构建支付订单失败");
        }else{
            orderPOJO.setSign(sign);
            treemap.put("sign", sign);
            payMapper.insertNewOrder(orderPOJO);
            JSONObject json = JSON.parseObject(JSON.toJSONString(treemap));
            return AjaxResult.success("构建支付订单成功", json);
        }
    }


    //创建用户vip注册支付订单
    private AjaxResult createVipSignOrder(CreateOrderDTO createOrderDTO) {
//        OrderPOJO orderPOJO = payMapper.getDefaultOrderInfo();
        String param_contract = paramsMapper.getValue(ParamKeys.param_payment);  //从参数库表中获取支付相关信息
        OrderPOJO orderPOJO = JSONObject.toJavaObject(JSON.parseObject(param_contract), OrderPOJO.class);

        orderPOJO.setUser_id(createOrderDTO.getUser_id());
        orderPOJO.setPaytype(createOrderDTO.getPay_type());
        orderPOJO.setOrder_type(createOrderDTO.getOrder_type());

        TreeMap<String, String> treemap = new TreeMap<String, String>();
        treemap.put("cusid", orderPOJO.getCusid());
        treemap.put("appid", orderPOJO.getAppid());
        treemap.put("version", orderPOJO.getVersion());
        treemap.put("trxamt", orderPOJO.getTrxamt());

        String reqsn = generateTxnOrderId();
        treemap.put("reqsn", reqsn);
        orderPOJO.setReqsn(reqsn);
        orderPOJO.setExpiretime("");

        treemap.put("notify_url", orderPOJO.getNotify_url());
        treemap.put("validtime", orderPOJO.getValidtime());
        treemap.put("body", orderPOJO.getBody());
        treemap.put("limit_pay", orderPOJO.getLimit_pay());

        String randomstr = Utils.getRandomChar(10);
        treemap.put("randomstr", randomstr);
        orderPOJO.setRandomstr(randomstr);

        treemap.put("paytype", orderPOJO.getPaytype());
        treemap.put("signtype", orderPOJO.getSigntype());
        treemap.put("isdirectpay", orderPOJO.getIsdirectpay());


        String sign = SybUtil.unionSign(treemap, orderPOJO.getAppkey(), orderPOJO.getSigntype());
        if(sign == null){
            return AjaxResult.error("构建支付订单失败");
        }else{
            orderPOJO.setSign(sign);
            payMapper.insertNewOrder(orderPOJO);
            treemap.put("sign", sign);
            JSONObject json = JSON.parseObject(JSON.toJSONString(treemap));
            return AjaxResult.success("构建支付订单成功", json);
        }
    }

    @Override
    public String callback(PayCallBackPOJO payCallBackDTO) {

        OrderPOJO orderPOJO = payMapper.getOrderInfoByReqsn(payCallBackDTO.getOuttrxid());
        switch (orderPOJO.getOrder_type()){
            case "vip_sign":
                return process_vip_sign_callback(payCallBackDTO);
            case "contract_guarantee_fund":
                return process_contract_guarantee_fund_callback(orderPOJO, payCallBackDTO);
            default:
                return "success";
        }
    }

    //合同保障金用户支付成功，拿到回到回调参数后，进行相关处理
    private String process_contract_guarantee_fund_callback(OrderPOJO orderPOJO,
                                                            PayCallBackPOJO payCallBackDTO) {
        try{
            if(callback_success_code.equals(payCallBackDTO.getTrxstatus())){
                String contract_id = orderPOJO.getContract_id();
                ContractPOJO contractPOJO = contractMapper.getContractInfo(contract_id);
                int user_id = orderPOJO.getUser_id();
                long buyer_id = contractPOJO.getBuyer_id();
                long seller_id = contractPOJO.getSeller_id();
                if(buyer_id == user_id){
                    contractService.buyer_pay_fund_success(contract_id);
                }else if(seller_id == user_id){
                    contractService.seller_pay_fund_success(contract_id);
                }
            }

            payMapper.insertPayCallback(payCallBackDTO);
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }

        return "success";

    }

    //vip用户注册支付成功，拿到回到回调参数后，进行相关处理
    private String process_vip_sign_callback(PayCallBackPOJO payCallBackDTO) {
        try{
            if(callback_success_code.equals(payCallBackDTO.getTrxstatus())){
                String user_id = payMapper.getUserIdByReqsn(payCallBackDTO.getOuttrxid());
                if(user_id != null){
                    userMapper.updateRole(Long.valueOf(user_id), 1);
                }
            }

            payMapper.insertPayCallback(payCallBackDTO);

        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }

        return "success";
    }


    public String generateTxnOrderId(){
        LocalDateTime localDateTime = LocalDateTime.now();

        String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomChar = Utils.getRandomChar(10);
        return time + randomChar;
    }


    @Override
    public AjaxResult getOrderStatus(String reqsn){
        try{
            OrderPOJO orderPOJO = payMapper.getOrderInfoByReqsn(reqsn);
            if(orderPOJO == null){
                return AjaxResult.error("订单有误，请重新注册");
            }

            String response = TonglianHttpRequest.getOrderStatus(orderPOJO);
            JSONObject response_json = JSON.parseObject(response);
            if(response_json.containsKey("trxstatus")){
                String trxstatus = response_json.getString("trxstatus");
                if(callback_success_code.equals(trxstatus)){
                    return AjaxResult.success("订单支付成功");
                }else if(callback_unpay.equals(trxstatus)){
                    return new AjaxResult(201, "订单未支付", null);
                }
            }
            return AjaxResult.error("订单有误，请重新注册");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("订单有误，请重新注册");
        }
    }

    @Override
    public AjaxResult price() {
//        OrderPOJO orderPOJO = payMapper.getDefaultOrderInfo();
        String param_contract = paramsMapper.getValue(ParamKeys.param_payment);  //从参数库表中获取支付相关信息
        OrderPOJO orderPOJO = JSONObject.toJavaObject(JSON.parseObject(param_contract), OrderPOJO.class);
        JSONObject json = new JSONObject();
        double price = Double.valueOf(orderPOJO.getTrxamt()) / 100;
        double original_price = Double.valueOf(orderPOJO.getOriginal_price()) / 100;

        json.put("price",price);
        json.put("original_price", original_price);

        return AjaxResult.success("获取支付价格成功", json);
    }


}
