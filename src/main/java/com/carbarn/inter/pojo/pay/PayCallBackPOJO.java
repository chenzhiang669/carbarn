package com.carbarn.inter.pojo.pay;

import lombok.Data;

@Data
public class PayCallBackPOJO {
    private String signAture;
    private String resv;
    private String settleTime;
    private String chnlMerId;
    private String openId;
    private String settleAmt;
    private String settleDate;
    private String txnType;
    private String orderStat;
    private String wxOrderId;
    private String txnOrderTime;
    private String respTxnSsn;
    private String mchtRemarks;
    private String respMsg;
    private String settleCcyType;
    private String merId;
    private String tpamTxnSsn;
    private String txnOrderId;
    private String respCode;
    private String respTxnTime;
}
