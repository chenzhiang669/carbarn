package com.carbarn.contract.contants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cn.dev33.satoken.secure.SaSecureUtil.md5;

public class ContractUtils {

    public static DateTimeFormatter dateFormater = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter secondFormater = DateTimeFormatter.ofPattern("HHmmss");

    public static final long day_milliseconds = 1 * 24 * 60 * 60 * 1000;

    public static String createContractId(int car_id,
                                          long buyer_id,
                                          long seller_id){
        LocalDateTime localDateTime = LocalDateTime.now();
        String date = localDateTime.format(dateFormater);
        String second = localDateTime.format(secondFormater);
        String str = second + "-" + car_id + "-" + buyer_id + "-" + seller_id;
        String md5 = md5(str);
        String contract_id = date + md5.substring(0,6);
        return contract_id;
    }

    public static void main(String[] args) {
        System.out.println(createContractId(23, 545, 65));
    }
}
