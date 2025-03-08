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

public class InsertTypeDetails {

    private static final String URL = "jdbc:mysql://localhost:3306/carbarn";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) throws SQLException, IOException {
        String sql = "update car_type_qixiubao " +
                "set details = ? " +
                "where type_id = ?";
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);


        File file = new File("D:\\carbarn\\汽修宝\\type_detail_original_data\\");
        String[] files = file.list();
        int count = 0;
        for (String filename : files) {
            if ("type_detail_original_data1".equals(filename)) {
                continue;
            }

            System.out.println(filename);

            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\type_detail_original_data\\" + filename)));


            String line = null;
            while ((line = br.readLine()) != null) {
                count = count + 1;

                try{
                    String[] infos = line.split("\t");
                    int type_id = Integer.valueOf(infos[0]);
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
                                my_data.put("name", name);
                                my_data.put("value", value);

                                my_params.add(my_data);
                            }

                            JSONObject my_in = new JSONObject();


                            my_in.put("key", my_key);
                            my_in.put("params", my_params);
                            my_result.add(my_in);
                        }

//                    System.out.println(my_result.toJSONString());
                        pstmt.setString(1, my_result.toJSONString());
                        pstmt.setInt(2, type_id);

                        pstmt.addBatch();

                        if (count % 1000 == 0) {
                            System.out.println(count);
                            pstmt.executeBatch();
                        }
                    }
                }catch (Exception e){
                    continue;
                }



            }
        }


        pstmt.executeBatch();
    }
}
