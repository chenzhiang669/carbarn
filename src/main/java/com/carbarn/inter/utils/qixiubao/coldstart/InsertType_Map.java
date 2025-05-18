package com.carbarn.inter.utils.qixiubao.coldstart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.qixiubao.Contants;
import io.swagger.models.auth.In;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InsertType_Map {

    public static HashMap<String, Integer> trans_des = new HashMap<String, Integer>();
    public static HashMap<String, Integer> environmental_standards = new HashMap<String, Integer>();
    public static HashMap<String, Integer> djit = new HashMap<String, Integer>();
    public static HashMap<String, Integer> fuel_type = new HashMap<String, Integer>();
    public static HashMap<String, Integer> driver = new HashMap<String, Integer>();
    public static HashMap<String, Integer> structure = new HashMap<String, Integer>();
    public static HashMap<String, Integer> level = new HashMap<String, Integer>();

    public static HashMap<String, Integer> engine = new HashMap<String, Integer>();


    public static void main(String[] args) throws SQLException, IOException {

        File file = new File("D:\\carbarn\\汽修宝\\type_original_data\\");
        String[] files = file.list();
        int count = 0;
        for (String filename : files) {
//            System.out.println(filename);

            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\type_original_data\\" + filename)));


            String line = null;
            while ((line = br.readLine()) != null) {
                JSONObject jsonObject = JSON.parseObject(line.trim());
                if (jsonObject.containsKey("result")) {
                    JSONObject result = jsonObject.getJSONObject("result");
                    JSONArray list = result.getJSONArray("list");

                    for (int i = 0; i < list.size(); i++) {
                        JSONObject data = list.getJSONObject(i);
                        if(data.getInteger("id") == 71460){
                            System.out.println(data);
                        }

                        String key_trans_des = data.getString("trans_des");
                        if (trans_des.containsKey(key_trans_des)){
                            trans_des.put(key_trans_des, trans_des.get(key_trans_des) + 1);
                        }else{
                            trans_des.put(key_trans_des, 1);
                        }

                        String key_environmental_standards = data.getString("environmental_standards");
                        if (environmental_standards.containsKey(key_environmental_standards)){
                            environmental_standards.put(key_environmental_standards, environmental_standards.get(key_environmental_standards) + 1);
                        }else{
                            environmental_standards.put(key_environmental_standards, 1);
                        }

                        String key_djit = data.getString("djit");
                        if (djit.containsKey(key_djit)){
                            djit.put(key_djit, djit.get(key_djit) + 1);
                        }else{
                            djit.put(key_djit, 1);
                        }


                        String key_fuel_type = data.getString("fuel_type");
                        if (fuel_type.containsKey(key_fuel_type)){
                            fuel_type.put(key_fuel_type, fuel_type.get(key_fuel_type) + 1);
                        }else{
                            fuel_type.put(key_fuel_type, 1);
                        }

                        String key_driver = data.getString("driver");
                        if (driver.containsKey(key_driver)){
                            driver.put(key_driver, driver.get(key_driver) + 1);
                        }else{
                            driver.put(key_driver, 1);
                        }

                        String key_structure = data.getString("structure");
                        if (structure.containsKey(key_structure)){
                            structure.put(key_structure, structure.get(key_structure) + 1);
                        }else{
                            structure.put(key_structure, 1);
                        }


                        String key_level = data.getString("level");
                        if (level.containsKey(key_level)){
                            level.put(key_level, level.get(key_level) + 1);
                        }else{
                            level.put(key_level, 1);
                        }


                        String key_engine = data.getString("engine");
                        if (engine.containsKey(key_engine)){
                            engine.put(key_engine, engine.get(key_engine) + 1);
                        }else{
                            engine.put(key_engine, 1);
                        }
//                        trans_des.add(data.getString("trans_des"));
//                        environmental_standards.add(data.getString("environmental_standards"));
//                        djit.add(data.getString("djit"));
//                        fuel_type.add(data.getString("fuel_type"));
//                        driver.add(data.getString("driver"));
//                        structure.add(data.getString("structure"));
//                        level.add(data.getString("level"));
                    }
                }
            }
        }


//        trans_des.forEach((x,y) -> {
//            System.out.println(x + "\t" + y);
//        });
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        environmental_standards.forEach((x,y) -> {
//            System.out.println(x + "\t" + y);
//        });
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        djit.forEach((x,y) -> {
//            System.out.println(x + "\t" + y);
//        });
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        fuel_type.forEach((x,y) -> {
//            System.out.println(x + "\t" + y);
//        });
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        driver.forEach((x,y) -> {
//            System.out.println(x + "\t" + y);
//        });
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        structure.forEach((x,y) -> {
//            System.out.println(x + "\t" + y);
//        });
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        level.forEach((x,y) -> {
//            System.out.println(x + "\t" + y);
//        });
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        engine.forEach((x,y) -> {
//            System.out.println(x + "\t" + y);
//        });
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
