package com.carbarn.inter.utils.qixiubao;

import java.util.HashMap;
import java.util.Map;

public class Contants {
    public static Map<String, String> emission_standards = new HashMap<String, String>();
    public static Map<String, String> engines = new HashMap<String, String>();

    public static Map<String, String> type_of_cars = new HashMap<String, String>();


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
    }
}
