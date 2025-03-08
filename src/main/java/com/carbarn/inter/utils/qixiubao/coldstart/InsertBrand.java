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

public class InsertBrand {
    private static final String URL = "jdbc:mysql://localhost:3306/carbarn";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";


    public static void main(String[] args) throws SQLException, IOException {
        String sql = "INSERT INTO car_brand_qixiubao (first_char, brand, brand_id, logo, `language`) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);

        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\brand_original_data")));
        String line = null;
        while ((line = br.readLine()) != null) {
            JSONObject jsonObject = JSON.parseObject(line.trim());
            if (jsonObject.containsKey("result")) {
                JSONArray result = jsonObject.getJSONArray("result");
                for (int i = 0; i < result.size(); i++) {
                    JSONObject data = result.getJSONObject(i);
                    String first_char = data.getString("first_letter");
                    String logo = data.getString("img_url");
                    int brand_id = data.getInteger("id");
                    String brand = data.getString("name");

                    pstmt.setString(1, first_char);
                    pstmt.setString(2, brand);
                    pstmt.setInt(3, brand_id);
                    pstmt.setString(4, logo);
                    pstmt.setString(5, "zh");
                    pstmt.addBatch();
                }
            }
            // 执行批处理
            pstmt.executeBatch();

        }
    }
}
