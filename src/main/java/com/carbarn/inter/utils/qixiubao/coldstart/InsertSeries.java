package com.carbarn.inter.utils.qixiubao.coldstart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertSeries {

    private static final String URL = "jdbc:mysql://localhost:3306/carbarn";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";


    public static void main(String[] args) throws SQLException, IOException {
        String sql = "INSERT INTO car_series_qixiubao (first_char, series, series_id, brand_id, `language`) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);

        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\series_original_data")));
        String line = null;
        int count = 0;
        while ((line = br.readLine()) != null) {
            JSONObject jsonObject = JSON.parseObject(line.trim());
            if (jsonObject.containsKey("result")) {
                JSONArray result = jsonObject.getJSONArray("result");
                for (int i = 0; i < result.size(); i++) {
                    JSONObject data = result.getJSONObject(i);

                    int brand_id = data.getInteger("brand_id");
                    String series = data.getString("name");
                    int series_id = data.getInteger("id");
                    String first_char = Utils.getFirstLetter(series);

                    pstmt.setString(1, first_char);
                    pstmt.setString(2, series);
                    pstmt.setInt(3, series_id);
                    pstmt.setInt(4, brand_id);
                    pstmt.setString(5, "zh");
                    pstmt.addBatch();

                    count = count + 1;
                    if(count % 1000 == 0){
                        System.out.println(count);
                        pstmt.executeBatch();
                    }
                }
            }
        }

        pstmt.executeBatch();
    }
}
