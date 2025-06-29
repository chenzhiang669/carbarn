package com.carbarn.inter.pojo.pay;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class PayCallBackGlobalPOJO {
    private String resultCode;
    private String resultDesc;
    private String mchtId;
    private String accessOrderId;
    private String orderId;
    private String cardNo;
    private String cardOrgn;
    private String currency;
    private String amount;
    private String caAuthCode;
    private String LocalCurrency;
    private String LocalAmount;
    private String transTime;
    private String sign;
    private String signType;
}
