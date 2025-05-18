package com.carbarn.inter.utils.qixiubao.coldstart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.qixiubao.Contants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

public class InsertType_New {

    private static final String URL = "jdbc:mysql://localhost:3306/carbarn";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static final HashMap<String, String> qixiubao_carbarn = new HashMap<String, String>(); //汽修宝--车出海映射关系
    public static final HashMap<String, Integer> carbarn_id = new HashMap<String, Integer>(); //车出海字段--车出海id映射关系


    public static void init() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\汽修宝-车出海字段值映射关系-人工标注")));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] infos = line.split("\t");
            String qixiubao = infos[0];
            String carbarn = infos[1];

            qixiubao_carbarn.put(qixiubao, carbarn);
        }

        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from index_new where `language` = 'zh'");
        while(resultSet.next()){
            int value_id = resultSet.getInt("value_id");
            String value = resultSet.getString("value");
            String field = resultSet.getString("field");

            carbarn_id.put(field + "_" + value, value_id);
        }


        carbarn_id.forEach((x, y) -> {
            System.out.println(x + "\t" + y);
        });

        conn.close();
    }


    public static int getFieldId(JSONObject data,
                                 String key_qixiubao,
                                 String field_carbarn,
                                 String default_carbarn_value){
        int id = -1;
        try {
            String value_qixiubao = data.getString(key_qixiubao);
            if(qixiubao_carbarn.containsKey(value_qixiubao)){
                String carbarn_value = qixiubao_carbarn.get(value_qixiubao);
                String key_carbarn = field_carbarn + "_" + carbarn_value;
                if(carbarn_id.containsKey(key_carbarn)){
                    id = carbarn_id.get(key_carbarn);
                }
            }
        } catch (Exception e) {
            id = carbarn_id.get(field_carbarn + "_" + default_carbarn_value);
        }

        if(id == -1){
            id = carbarn_id.get(field_carbarn + "_" + default_carbarn_value);
        }

        return id;
    }


    public static void main(String[] args) throws SQLException, IOException {
        init();
        String sql = "INSERT INTO car_type_qixiubao " +
                "(type,type_id,series_id,brand_id,year,language,price," +
                "floor_price,guide_price,displacement,displacement_type,power,battery_capacity," +
                "pure_electric_range,color,transmission,emission_standards,type_of_manu,engine," +
                "drive_type,box,type_of_car,seat_capacity) " +
                "VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);


        File file = new File("D:\\carbarn\\汽修宝\\type_original_data\\");
        String[] files = file.list();
        int count = 0;
        for (String filename : files) {
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

                        int transmission = getFieldId(data,"trans_des", "transmission","自动");
                        pstmt.setInt(16, transmission);

                        int emission_standards = getFieldId(data,"environmental_standards", "emission_standards","其他");
                        pstmt.setInt(17, emission_standards);


                        int type_of_manu = getFieldId(data,"djit", "type_of_manu","国产");
                        pstmt.setInt(18, type_of_manu);

                        String _engine = data.getString("engine");
                        if("纯电动".equals(_engine)){
                            int engine = carbarn_id.get("engine_电动");
                            pstmt.setInt(19, engine);
                        }else{
                            int engine = getFieldId(data,"fuel_type", "engine","其他");
                            pstmt.setInt(19, engine);
                        }



                        int drive_type = getFieldId(data,"driver", "drive_type","未知");
                        pstmt.setInt(20, drive_type);



                        int box = -1;
                        try {
                            String _structure = data.getString("structure");
                            if (_structure.contains("单厢")) {
                                box = carbarn_id.get("box_单厢");
                            } else if (_structure.contains("两厢")) {
                                box = carbarn_id.get("box_两厢");
                            } else if (_structure.contains("三厢")) {
                                box = carbarn_id.get("box_三厢");
                            } else {
                                box = carbarn_id.get("box_未知");
                            }
                        } catch (Exception e) {
                            box = carbarn_id.get("box_未知");
                        }
                        pstmt.setInt(21, box);


                        int type_of_car = getFieldId(data,"level", "type_of_car","不限");;
                        pstmt.setInt(22, type_of_car);

                        String seat_capacity_string = data.getString("seats");
                        if(seat_capacity_string == null || "".equals(seat_capacity_string.trim())){
                            seat_capacity_string = "0";
                        }

                        int seat_capacity = 0;
                        try {
                            seat_capacity = Integer.valueOf(seat_capacity_string);
                        }catch (Exception e){
                            seat_capacity = 0;
                        }
                        pstmt.setInt(23, seat_capacity);

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
