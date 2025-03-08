package com.carbarn.inter.utils.qixiubao;

import java.util.HashMap;
import java.util.Map;

public class Contants {

//    public static final String url = "http://test-open-api2.0.nanxinwang.com//VinDecoder/decode";
//    public static final String secret = "c60e8d3539b3748e882ed3beb4ac3f6c";
//    public static final String appid = "7f5a424c687328576f337b0986dd14bd";

    public static final String vin_url = "http://open-api2.0.nanxinwang.com//VinDecoder/decode";
    public static final String vin_version = "4.6.0";
    public static final String secret = "533f15bd38980184203ada7b0fe029de";
    public static final String appid = "663f8c5be37ec1db7c6b38704c7a1c28";

    public static Map<String, String> emission_standards = new HashMap<String, String>();
    public static Map<String, String> engines = new HashMap<String, String>();

    public static Map<String, String> type_of_cars = new HashMap<String, String>();

    public static Map<String, String> transmission = new HashMap<String, String>();




    static{
        emission_standards.put("国I", "国一");
        emission_standards.put("国II", "国二");
        emission_standards.put("国III", "国三");
        emission_standards.put("国IV", "国四");
        emission_standards.put("国IV(国V)", "国四");
        emission_standards.put("国IV(国V)+OBD", "国四");
        emission_standards.put("国IV(京V)", "国四");
        emission_standards.put("国IV/国V", "国四");
        emission_standards.put("国IV/京V", "国四");
        emission_standards.put("国IV+OBD", "国四");
        emission_standards.put("国V", "国五");
        emission_standards.put("国V(国VI)", "国五");
        emission_standards.put("国V+OBD", "国五");
        emission_standards.put("京V", "国五");
        emission_standards.put("国VI", "国六");
        emission_standards.put("国VIB", "国六");
        emission_standards.put("欧I", "欧一");
        emission_standards.put("欧II", "欧二");
        emission_standards.put("欧III", "欧三");
        emission_standards.put("欧III+OBD", "欧三");
        emission_standards.put("欧IV", "欧四");
        emission_standards.put("欧IV+OBD", "欧四");

        engines.put("汽油","汽油");
        engines.put("柴油","柴油");
        engines.put("混动","混动");
        engines.put("电动","电动");

        type_of_cars.put("MPV","商务车/MPV");
        type_of_cars.put("SUV","越野车/SUV");
        type_of_cars.put("大型SUV","越野车/SUV");
        type_of_cars.put("房车","房车");
        type_of_cars.put("轿车","轿车");
        type_of_cars.put("紧凑型SUV","越野车/SUV");
        type_of_cars.put("紧凑型车","轿车");
        type_of_cars.put("跑车","跑车");
        type_of_cars.put("皮卡","皮卡");
        type_of_cars.put("轻货","货车");
        type_of_cars.put("轻卡","皮卡");
        type_of_cars.put("轻客","客车");
        type_of_cars.put("微卡","皮卡");
        type_of_cars.put("微面","面包车");
        type_of_cars.put("小型车","轿车");

        transmission.put("10挡手动","手动");
        transmission.put("10挡手自一体","自动");
        transmission.put("10挡无级变速","自动");
        transmission.put("10挡自动","自动");
        transmission.put("12挡手动","手动");
        transmission.put("12挡手自一体","自动");
        transmission.put("12挡自动","自动");
        transmission.put("13挡手动","手动");
        transmission.put("14挡手动","手动");
        transmission.put("14挡手自一体","自动");
        transmission.put("16挡手动","手动");
        transmission.put("16挡手自一体","自动");
        transmission.put("16挡自动","自动");
        transmission.put("1挡DHT","自动");
        transmission.put("1挡单速","自动");
        transmission.put("1挡电动车单速","自动");
        transmission.put("2挡DHT","自动");
        transmission.put("2挡自动","自动");
        transmission.put("3挡DHT","自动");
        transmission.put("3挡自动","自动");
        transmission.put("4挡DHT","自动");
        transmission.put("4挡手动","手动");
        transmission.put("4挡手自一体","自动");
        transmission.put("4挡无级变速","自动");
        transmission.put("4挡自动","自动");
        transmission.put("5挡AMT","自动");
        transmission.put("5挡半自动","自动");
        transmission.put("5挡湿式双离合","自动");
        transmission.put("5挡手动","手动");
        transmission.put("5挡手自一体","自动");
        transmission.put("5挡双离合","自动");
        transmission.put("5挡无级/手动一体式","自动");
        transmission.put("5挡序列","手动");
        transmission.put("5挡自动","自动");
        transmission.put("6挡AMT","自动");
        transmission.put("6挡DSG","自动");
        transmission.put("6挡半自动","自动");
        transmission.put("6挡干式双离合","自动");
        transmission.put("6挡连续手自一体","自动");
        transmission.put("6挡三离合电驱变速器","自动");
        transmission.put("6挡湿式双离合","自动");
        transmission.put("6挡手动","手动");
        transmission.put("6挡手自一体","自动");
        transmission.put("6挡双离合","自动");
        transmission.put("6挡无级/手动一体式","自动");
        transmission.put("6挡无级变速","自动");
        transmission.put("6挡序列","手动");
        transmission.put("6挡序列变速","手动");
        transmission.put("6挡序列变速箱(AMT)","自动");
        transmission.put("6挡自动","自动");
        transmission.put("7挡AMT","自动");
        transmission.put("7挡DSG","自动");
        transmission.put("7挡ISR变速箱","自动");
        transmission.put("7挡干式双离合","自动");
        transmission.put("7挡湿式双离合","自动");
        transmission.put("7挡手动","手动");
        transmission.put("7挡手自一体","自动");
        transmission.put("7挡双离合","自动");
        transmission.put("7挡无级变速","自动");
        transmission.put("7挡序列","手动");
        transmission.put("7挡自动","自动");
        transmission.put("8挡湿式双离合","自动");
        transmission.put("8挡手动","手动");
        transmission.put("8挡手自一体","自动");
        transmission.put("8挡双离合变速箱","自动");
        transmission.put("8挡自动","自动");
        transmission.put("9挡湿式双离合","自动");
        transmission.put("9挡手动","手动");
        transmission.put("9挡手自一体","自动");
        transmission.put("9挡自动","自动");
        transmission.put("AMT（组合10挡）","自动");
        transmission.put("CVT无级变速","自动");
        transmission.put("CVT无级变速(模拟10挡)","自动");
        transmission.put("CVT无级变速(模拟4挡)","自动");
        transmission.put("CVT无级变速(模拟5挡)","自动");
        transmission.put("CVT无级变速(模拟6挡)","自动");
        transmission.put("CVT无级变速(模拟7挡)","自动");
        transmission.put("CVT无级变速(模拟7挡）","自动");
        transmission.put("CVT无级变速(模拟8挡)","自动");
        transmission.put("CVT无级变速(模拟9挡)","自动");
        transmission.put("CVT无级变速模拟7挡","自动");
        transmission.put("DHT","自动");
        transmission.put("ECVT","自动");
        transmission.put("E-CVT+自动变速箱（模拟10挡）","自动");
        transmission.put("E-CVT+自动变速箱(模拟10挡)","自动");
        transmission.put("E-CVT无级变速","自动");
        transmission.put("电动车单速","自动");
        transmission.put("电动车单速变速箱","自动");
        transmission.put("电动车单速变速箱（模拟5挡）","自动");
        transmission.put("电子无级变速","自动");
        transmission.put("模拟10挡挡自动","自动");
        transmission.put("模拟6挡挡自动","自动");
        transmission.put("模拟6档挡自动","自动");
        transmission.put("模拟7挡挡自动","自动");
        transmission.put("模拟7档挡自动","自动");
        transmission.put("模拟8挡挡自动","自动");
        transmission.put("模拟8档挡自动","自动");
        transmission.put("模拟9档挡自动","自动");
        transmission.put("手动","手动");
        transmission.put("手自一体","自动");
        transmission.put("无级/手动一体式","自动");
        transmission.put("无级变速","自动");
        transmission.put("自动","自动");

    }
}
