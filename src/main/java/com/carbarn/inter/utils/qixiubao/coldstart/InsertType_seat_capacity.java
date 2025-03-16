package com.carbarn.inter.utils.qixiubao.coldstart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.qixiubao.Contants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class InsertType_seat_capacity {

    private static final String URL = "jdbc:mysql://47.122.70.102:3306/carbarn";
    private static final String USER = "root";
    private static final String PASSWORD = "Chechuhai123";

    public static final HashMap<String, Integer> maps = new HashMap<String, Integer>();

    public static void main(String[] args) throws SQLException, IOException {
        String sql = "update car_type " +
                "set seat_capacity = ? " +
                "where type_id = ? ";
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);


        File file = new File("D:\\carbarn\\汽修宝\\type_original_data\\");
        String[] files = file.list();
        int count = 0;
        for (String filename : files) {
            if (!"type_original_data1".equals(filename)) {
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
                        int type_id = data.getInteger("id");
//                        System.out.println(type_id);
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



                        pstmt.setInt(1, seat_capacity);
                        pstmt.setInt(2, type_id);

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
