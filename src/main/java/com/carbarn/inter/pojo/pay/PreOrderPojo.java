package com.carbarn.inter.pojo.pay;

import lombok.Data;

@Data
public class PreOrderPojo {
    private int id;
    private int user_id;
    private String encoding;
    private String sdkAppId;
    private String certId;
    private String signAture;
    private String txnType;
    private String txnSubType;
    private String aesWay;
    private String merId;
    private String merName;
    private String secMerId;
    private String backEndUrl;
    private String frontEndUrl;
    private String txnOrderId;
    private String txnOrderTime;
    private String txnOrderBody;
    private String txnAmt;
    private String txnCcyType;
    private String tokenCode;
    private String tokenEnd;
    private String tokenType;
    private String tokenBegin;
    private String respTxnSsn;
    private String respTxnTime;
    private String respMsg;
}
