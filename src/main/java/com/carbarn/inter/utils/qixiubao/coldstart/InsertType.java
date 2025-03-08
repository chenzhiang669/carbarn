package com.carbarn.inter.utils.qixiubao.coldstart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.qixiubao.Contants;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class InsertType {

    private static final String URL = "jdbc:mysql://localhost:3306/carbarn";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static final HashMap<String, Integer> maps = new HashMap<String, Integer>();

    static {
        maps.put("type_of_car_轿车", 1);
        maps.put("type_of_car_越野车/SUV", 2);
        maps.put("type_of_car_商务车/MPV", 3);
        maps.put("type_of_car_跑车", 4);
        maps.put("type_of_car_皮卡", 5);
        maps.put("type_of_car_面包车", 6);
        maps.put("type_of_car_客车", 7);
        maps.put("type_of_car_货车", 8);
        maps.put("type_of_car_房车", 9);
        maps.put("type_of_car_敞篷", 10);
        maps.put("type_of_car_轿跑车", 11);
        maps.put("type_of_car_不限", 12);
        maps.put("type_of_car_旅行车", 13);
        maps.put("type_of_car_掀背", 14);
        maps.put("color_黑色", 25);
        maps.put("color_白色", 26);
        maps.put("color_银白色", 27);
        maps.put("color_红色", 28);
        maps.put("color_蓝色", 29);
        maps.put("color_橙色", 30);
        maps.put("color_紫色", 31);
        maps.put("color_金色", 32);
        maps.put("color_灰色", 33);
        maps.put("color_棕色", 34);
        maps.put("color_绿色", 35);
        maps.put("color_黄色", 36);
        maps.put("color_咖啡色", 37);
        maps.put("color_多色", 38);
        maps.put("color_其他", 39);
        maps.put("origin_country_国产车", 40);
        maps.put("origin_country_德系车", 41);
        maps.put("origin_country_日系车", 42);
        maps.put("origin_country_欧系车", 43);
        maps.put("origin_country_法系车", 44);
        maps.put("origin_country_美系车", 45);
        maps.put("origin_country_韩系车", 46);
        maps.put("origin_country_其他", 47);
        maps.put("transmission_自动", 48);
        maps.put("transmission_手动", 49);
        maps.put("type_of_manu_国产", 50);
        maps.put("type_of_manu_进口", 51);
        maps.put("type_of_manu_合资", 52);
        maps.put("emission_standards_国一", 60);
        maps.put("emission_standards_国三", 61);
        maps.put("emission_standards_国四", 62);
        maps.put("emission_standards_国五", 63);
        maps.put("emission_standards_国六", 64);
        maps.put("emission_standards_欧一", 65);
        maps.put("emission_standards_欧二", 66);
        maps.put("emission_standards_欧三", 67);
        maps.put("emission_standards_欧四", 68);
        maps.put("emission_standards_纯电动", 69);
        maps.put("emission_standards_其他", 70);
        maps.put("engine_汽油", 71);
        maps.put("engine_柴油", 72);
        maps.put("engine_电动", 73);
        maps.put("engine_混动", 74);
        maps.put("engine_其他", 75);
        maps.put("drive_type_前驱", 76);
        maps.put("drive_type_后驱", 77);
        maps.put("drive_type_四驱", 78);
        maps.put("drive_type_未知", 79);
        maps.put("box_单厢", 80);
        maps.put("box_两厢", 81);
        maps.put("box_三厢", 82);
        maps.put("box_未知", 83);
        maps.put("price_unknown", 84);
        maps.put("floor_price_unknown", 85);
        maps.put("guide_price_unknown", 86);
        maps.put("displacement_unknown", 87);
        maps.put("displacement_type_unknown", 88);
        maps.put("power_unknown", 89);
        maps.put("battery_capacity_unknown", 90);
        maps.put("pure_electric_range_unknown", 91);
        maps.put("label_高保值", 92);
        maps.put("label_代步车", 93);
        maps.put("label_准新车", 94);
        maps.put("label_全车原漆", 95);
        maps.put("label_0过户", 96);
        maps.put("label_低油耗", 97);
        maps.put("label_高性价比", 98);
        maps.put("label_全车原版", 99);
        maps.put("label_车况精品", 100);
        maps.put("label_一手车", 101);
        maps.put("label_实表里程", 102);
        maps.put("label_平行进口车", 103);
        maps.put("label_营转非", 104);
        maps.put("label_事故车", 105);
        maps.put("label_瑕疵车", 106);
        maps.put("label_泡水车", 107);
        maps.put("label_火烧车", 108);
        maps.put("car_condition_非事故车", 109);
        maps.put("car_condition_碰撞事故", 110);
        maps.put("coating_全车原漆", 111);
        maps.put("coating_划痕", 112);
        maps.put("coating_凹痕", 113);
        maps.put("component_原版", 114);
        maps.put("component_受损", 115);
        maps.put("component_换件", 116);
        maps.put("engine_condition_正常", 117);
        maps.put("engine_condition_异常", 118);
        maps.put("engine_condition_拆卸", 119);
        maps.put("transmission_condition_正常", 120);
        maps.put("transmission_condition_异常", 121);
        maps.put("transmission_condition_拆卸", 122);
        maps.put("number_of_transfers_一手车", 123);
        maps.put("number_of_transfers_一次过户", 124);
        maps.put("number_of_transfers_多次过户", 125);
        maps.put("mileage_contition_实表", 126);
        maps.put("mileage_contition_调表", 127);
        maps.put("mileage_contition_表显", 128);
        maps.put("mileage_unknown", 129);
        maps.put("car_condition_泡水车", 130);

    }


    public static void main(String[] args) throws SQLException, IOException {
        String sql = "INSERT INTO car_type_qixiubao " +
                "(type,type_id,series_id,brand_id,year,language,price," +
                "floor_price,guide_price,displacement,displacement_type,power,battery_capacity," +
                "pure_electric_range,color,transmission,emission_standards,type_of_manu,engine," +
                "drive_type,box,type_of_car) " +
                "VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?, ?, ?)";
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);


        File file = new File("D:\\carbarn\\汽修宝\\type_original_data\\");
        String[] files = file.list();
        int count = 0;
        for (String filename : files) {
            if ("type_original_data1".equals(filename)) {
                continue;
            }

            System.out.println(filename);

            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\type_original_data\\" + filename)));


            String line = null;
            while ((line = br.readLine()) != null) {
                JSONObject jsonObject = JSON.parseObject(line.trim());
                if (jsonObject.containsKey("result")) {
                    JSONObject result = jsonObject.getJSONObject("result");
                    JSONArray list = result.getJSONArray("list");

                    for (int i = 0; i < list.size(); i++) {
                        JSONObject data = list.getJSONObject(i);

                        String type = data.getString("description");
                        pstmt.setString(1, type);

                        int type_id = data.getInteger("id");
                        pstmt.setInt(2, type_id);

                        int series_id = data.getInteger("series_id");
                        pstmt.setInt(3, series_id);

                        int brand_id = data.getInteger("brand_id");
                        pstmt.setInt(4, brand_id);

                        String year = data.getString("years");
                        pstmt.setString(5, year);

                        String language = "zh";
                        pstmt.setString(6, language);

                        double price = 0.0;
                        pstmt.setDouble(7, price);

                        double floor_price = 0.0;
                        pstmt.setDouble(8, floor_price);

                        double guide_price = 0.0;
                        try {
                            guide_price = Double.valueOf(data.getString("price").replaceAll("万", "")) * 10000;
                        } catch (Exception e) {
                            guide_price = 0.0;
                        }
                        pstmt.setDouble(9, guide_price);

                        double displacement = 0.0;
                        String displacement_type = "T";

                        try {
                            String _engine = data.getString("engine");
                            if (_engine.contains("T")) {
                                displacement_type = "T";
                                displacement = Double.valueOf(_engine.replaceAll("T", ""));
                            } else if (_engine.contains("L")) {
                                displacement_type = "L";
                                displacement = Double.valueOf(_engine.replaceAll("L", ""));
                            } else {
                                displacement = 0.0;
                                displacement_type = "T";
                            }
                        } catch (Exception e) {
                            displacement = 0.0;
                            displacement_type = "T";
                        }
                        pstmt.setDouble(10, displacement);
                        pstmt.setString(11, displacement_type);


                        double power = 0.0;
                        try {
                            power = Double.valueOf(data.getString("max_power"));
                        } catch (Exception e) {
                            power = 0.0;
                        }
                        pstmt.setDouble(12, power);

                        double battery_capacity = 0.0;
                        pstmt.setDouble(13, battery_capacity);

                        double pure_electric_range = 0.0;
                        pstmt.setDouble(14, pure_electric_range);

                        int color = -1;
                        pstmt.setInt(15, color);

                        int transmission = 48; //48：自动， 49：手动
                        try {
                            String _trans_des = data.getString("trans_des");
                            if (_trans_des.contains("手动")) {
                                transmission = 49;
                            } else if (_trans_des.contains("自动")) {
                                transmission = 48;
                            }
                        } catch (Exception e) {
                            transmission = 48;
                        }

                        pstmt.setInt(16, transmission);


                        int emission_standards = 60;
                        try {
                            String _environmental_standards = data.getString("environmental_standards");
                            if (Contants.emission_standards.containsKey(_environmental_standards)) {
                                String value = Contants.emission_standards.get(_environmental_standards);
                                if (maps.containsKey("emission_standards_" + value)) {
                                    emission_standards = maps.get("emission_standards_" + value);
                                }
                            }
                        } catch (Exception e) {

                        }
                        pstmt.setInt(17, emission_standards);


                        int type_of_manu = 50;
                        try {
                            String _djit = data.getString("djit");
                            if (_djit.contains("国产")) {
                                String value = "国产";
                                if (maps.containsKey("type_of_manu_" + value)) {
                                    type_of_manu = maps.get("type_of_manu_" + value);
                                }
                            } else if (_djit.contains("合资")) {
                                String value = "合资";
                                if (maps.containsKey("type_of_manu_" + value)) {
                                    type_of_manu = maps.get("type_of_manu_" + value);
                                }
                            } else if (_djit.contains("进口")) {
                                String value = "进口";
                                if (maps.containsKey("type_of_manu_" + value)) {
                                    type_of_manu = maps.get("type_of_manu_" + value);
                                }
                            }
                        } catch (Exception e) {
                        }

                        pstmt.setInt(18, type_of_manu);

                        int engine = 71;
                        try {
                            String _fuel_type = data.getString("fuel_type");
                            if (maps.containsKey("engine_" + _fuel_type)) {
                                engine = maps.get("engine_" + _fuel_type);
                            }
                        } catch (Exception e) {
                        }

                        pstmt.setInt(19, engine);


                        int drive_type = 76;
                        try {
                            String _driver = data.getString("driver");
                            if(_driver.contains("前驱")){
                                drive_type = maps.get("drive_type_前驱");
                            }else if(_driver.contains("后驱")){
                                drive_type = maps.get("drive_type_后驱");
                            }else if(_driver.contains("四驱")){
                                drive_type = maps.get("drive_type_四驱");
                            }else{
                                drive_type = maps.get("drive_type_未知");
                            }
                        } catch (Exception e) {
                        }

                        pstmt.setInt(20, drive_type);


                        int box = 82;
                        try {
                            String _structure = data.getString("structure");
                            if(_structure.contains("单厢")){
                                box = maps.get("box_单厢");
                            }else if(_structure.contains("两厢")){
                                box = maps.get("box_两厢");
                            }else if(_structure.contains("三厢")){
                                box = maps.get("box_三厢");
                            }else{
                                box = maps.get("box_未知");
                            }
                        } catch (Exception e) {
                        }

                        pstmt.setInt(21, box);


                        int type_of_car = 12;
                        try {
                            String _level = data.getString("level");
                            if (Contants.type_of_cars.containsKey(_level)) {
                                String value = Contants.type_of_cars.get(_level);
                                if (maps.containsKey("type_of_car_" + value)) {
                                    type_of_car = maps.get("type_of_car_" + value);
                                }
                            }
                        } catch (Exception e) {
                        }

                        pstmt.setInt(22, type_of_car);

                        pstmt.addBatch();

                        count = count + 1;

                        if (count % 1000 == 0) {
                            System.out.println(count);
                            pstmt.executeBatch();
                        }
                    }
                }
            }
        }


        pstmt.executeBatch();
    }
}
