package com.carbarn.inter.pojo.pay;

import lombok.Data;

@Data
public class OrderGlobalPOJO {
    private int user_id = -1;
    private String order_type;
    private String version;
    private String mchtId;
    private String transType;
    private String accessOrderId;
    private String currency;
    private String amount;
    private String language;
    private String payPageStyle;
    private String email;
    private String returnUrl;
    private String notifyUrl;
    private String productInfo;
    private String shippingFirstName;
    private String shippingLastName;
    private String shippingAddress1;
    private String shippingCity;
    private String shippingState;
    private String shippingCountry;
    private String shippingZipCode;
    private String shippingPhone;
    private String billingFirstName;
    private String billingLastName;
    private String billingAddress1;
    private String billingCity;
    private String billingState;
    private String billingCountry;
    private String billingZipCode;
    private String billingPhone;
    private String signtype;
    private String sign;
    private String contract_id;
    private String priv_key;
}
