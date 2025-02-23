package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        OrderPOJO orderPOJO = payMapper.getDefaultOrderInfo();

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

//        String expiretime = reqsn.substring(0,14);
//        treemap.put("expiretime", expiretime);
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
            return AjaxResult.error("构建订单失败");
        }else{
            orderPOJO.setSign(sign);
            payMapper.insertNewOrder(orderPOJO);
            treemap.put("sign", sign);
//            JSONObject json = (JSONObject)JSON.toJSON(orderPOJO);
//            json.remove("user_id");
//            json.remove("order_type");
//            json.remove("expiretime");
//            json.remove("appkey");
            JSONObject json = JSON.parseObject(JSON.toJSONString(treemap));
            return AjaxResult.success("构建订单成功", json);
        }
    }

    @Override
    public String callback(PayCallBackPOJO payCallBackDTO) {
        try{
            if("0000".equals(payCallBackDTO.getTrxstatus())){
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
                if("0000".equals(trxstatus)){
                    return AjaxResult.success("订单支付成功");
                }else if("2000".equals(trxstatus)){
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
        OrderPOJO orderPOJO = payMapper.getDefaultOrderInfo();
        JSONObject json = new JSONObject();
        double price = Double.valueOf(orderPOJO.getTrxamt()) / 100;
        double original_price = Double.valueOf(orderPOJO.getOriginal_price()) / 100;

        json.put("price",price);
        json.put("original_price", original_price);

        return AjaxResult.success("获取支付价格成功", json);
    }


}
