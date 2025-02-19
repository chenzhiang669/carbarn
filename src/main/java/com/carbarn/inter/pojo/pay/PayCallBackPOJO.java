package com.carbarn.inter.pojo.pay;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class PayCallBackPOJO {
    private	String	appid;
    private	String	outtrxid;
    private	String	trxcode;
    private	String	trxid;
    private	String	initamt;
    private	long	trxamt;
    private	String	trxdate;
    private	String	paytime;
    private	String	chnltrxid;
    private	String	trxstatus;
    private	String	cusid;
    private	String	termno;
    private	String	termbatchid;
    private	String	termtraceno;
    private	String	termauthno;
    private	String	termrefnum;
    private	String	trxreserved;
    private	String	srctrxid;
    private	String	cusorderid;
    private	String	acct;
    private	String	fee;
    private	String	signtype;
    private	String	cmid;
    private	String	chnlid;
    private	String	chnldata;
    private	String	accttype;
    private	String	bankcode;
    private	String	logonid;
    private	String	sign;
    private	String	tlopenid;

    public static void main(String[] args) {
        PayCallBackPOJO payCallBackPOJO = new PayCallBackPOJO();
        payCallBackPOJO.setAppid("00339478");
        payCallBackPOJO.setTrxcode("VSP511");
        payCallBackPOJO.setTrxid("adsfasdfa");
        payCallBackPOJO.setInitamt("1200.0");
        payCallBackPOJO.setTrxamt(1200);
        payCallBackPOJO.setTrxdate("20250119");
        payCallBackPOJO.setPaytime("20250119234534");
        payCallBackPOJO.setChnltrxid("zhifubao");
        payCallBackPOJO.setTrxstatus("aijfasdfa");
        payCallBackPOJO.setCusid("564361055214UTH");
        payCallBackPOJO.setCusorderid("20250124195412zGF0YzbpMw");
        payCallBackPOJO.setSign("7caa359a8d9f09cfd800c2309648b89f");

        System.out.println(JSON.toJSONString(payCallBackPOJO));
    }
}
