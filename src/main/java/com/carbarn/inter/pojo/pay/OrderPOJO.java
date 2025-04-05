package com.carbarn.inter.pojo.pay;

import lombok.Data;

@Data
public class OrderPOJO {
    private int user_id = -1;

    private String order_type;

    private String cusid = "564361055214UTH"; // 商户号

    private String appid = "00339478"; //应用id

    private String appkey = "allinpay1"; //appkey 做签名时使用

    private String version = "12"; //版本号

    private String trxamt = "1"; //付款金额

    private String reqsn; //商户订单号

    private String expiretime; //绝对时间

    private String notify_url = "http://47.122.70.102:8080/carbarn/pay/callback"; //异步通知url

    private String validtime = "5"; //订单有效时间(分钟)

    private String body = "车出海VIP用户注册";

    private String limit_pay = "no_credit";

    private String randomstr;

    private String paytype;

    private String signtype = "MD5";

    private String sign;

    private String isdirectpay = "1";

    private String original_price;

    private String contract_id;
}
