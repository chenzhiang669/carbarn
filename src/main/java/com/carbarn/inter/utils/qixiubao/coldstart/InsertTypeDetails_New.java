package com.carbarn.inter.utils.qixiubao.coldstart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertTypeDetails_New {

    private static final String URL = "jdbc:mysql://localhost:3306/carbarn";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) throws SQLException, IOException {
        String sql = "update car_type_qixiubao " +
                "set battery_capacity = ?, pure_electric_range = ?" +
                "where type_id = ?";
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);

        File file = new File("D:\\carbarn\\汽修宝\\type_detail_original_data\\");
        String[] files = file.list();
        int count = 0;
        for (String filename : files) {
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\type_detail_original_data\\" + filename)));


            String line = null;
            while ((line = br.readLine()) != null) {
                try{
                    String[] infos = line.split("\t");
                    int type_id = Integer.valueOf(infos[0]);
                    double battery_capacity = 0.0;
                    double pure_electric_range = 0.0;
                    String _line = infos[1].trim();
                    JSONObject jsonObject = JSON.parseObject(_line.trim());
                    if (jsonObject.containsKey("result")) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray params = result.getJSONArray("params");

                        JSONArray my_result = new JSONArray();
                        for (int i = 0; i < params.size(); i++) {
                            JSONObject a = params.getJSONObject(i);
                            String my_key = a.getString("type");
                            JSONArray items = a.getJSONArray("items");

                            JSONArray my_params = new JSONArray();

                            for(int j = 0; j < items.size(); j++){
                                JSONArray datas = items.getJSONArray(j);
                                JSONObject my_data = new JSONObject();
                                String name = datas.getString(0);
                                String value = datas.getString(1);
                                if(name.contains("电池能量(kWh)") && value != null){
                                    battery_capacity = Double.valueOf(value);
                                }else if(name.contains("续航") && value != null && Double.valueOf(value) > pure_electric_range){
                                    pure_electric_range = Double.valueOf(value);
                                }

                                my_params.add(my_data);
                            }

                        }

                    }

                    if(battery_capacity != 0.0 || pure_electric_range != 0.0){
                        count = count + 1;
                        pstmt.setDouble(1, battery_capacity);
                        pstmt.setDouble(2, pure_electric_range);
                        pstmt.setInt(3, type_id);
                        pstmt.addBatch();
                        System.out.println(type_id + "\t" + battery_capacity + "\t" + pure_electric_range);
                    }

                    if(count >= 1000){
                        System.out.println(count);
                        pstmt.executeBatch();
                        count = 0;
                    }
                }catch (Exception e){
                    continue;
                }

            }
        }

        pstmt.executeBatch();
    }
}
