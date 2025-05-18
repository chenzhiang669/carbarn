package com.carbarn.inter.utils.qixiubao.update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailsKeyValue {
    public static void main(String[] args) throws IOException {
        File dir = new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details\\");
        String[] files = dir.list();
        List<JSONObject> typedetails = new ArrayList<JSONObject>();
        Set<String> keywords = new HashSet<String>();
        for(String file : files) {
            System.out.println("process file: " + file);
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details\\" + file)));
            String line = null;
            while((line = br.readLine()) != null){
                JSONObject json = JSON.parseObject(line);
                JSONArray params = json.getJSONArray("params");
                for(int i = 0; i < params.size(); i++) {
                    JSONObject data = params.getJSONObject(i);
                    JSONArray params_in = data.getJSONArray("params");
                    for(int j = 0; j < params_in.size(); j++) {
                        JSONObject data1 = params_in.getJSONObject(j);
                        String name = data1.getString("name");
                        if(!"车型名称".equals(name)){
                            String value = data1.getString("value");
                            keywords.add(value);
                        }

                    }

                }
            }
        }



        for (String keyword : keywords) {
            System.out.println(keyword);
        }

        System.out.println(keywords.size());
    }
}
