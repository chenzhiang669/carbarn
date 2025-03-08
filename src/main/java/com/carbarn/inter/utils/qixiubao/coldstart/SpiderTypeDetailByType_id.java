package com.carbarn.inter.utils.qixiubao.coldstart;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SpiderTypeDetailByType_id {
    public static void main(String[] args) throws IOException {
        Logger wireLogger = (Logger) LoggerFactory.getLogger("org.apache.http.wire");
        wireLogger.setLevel(Level.OFF); // 禁用日志输出

        Logger headersLogger = (Logger) LoggerFactory.getLogger("org.apache.http.headers");
        headersLogger.setLevel(Level.OFF); // 禁用日志输出
        Logger headersimpl = (Logger) LoggerFactory.getLogger("org.apache.http.impl");
        headersimpl.setLevel(Level.OFF); // 禁用日志输出
        Logger headersclient = (Logger) LoggerFactory.getLogger("org.apache.http.client");
        headersclient.setLevel(Level.OFF); // 禁用日志输出

        File file = new File("D:\\carbarn\\汽修宝\\type_original_data\\");
        String[] files = file.list();
        int count = 0;
        for (String filename :files){
            if(!filename.contains("type_original")){
                continue;
            }

            System.out.println(filename);
            String bianhao = filename.replaceAll("type_original_data", "");
            if(Integer.valueOf(bianhao) > 99 || Integer.valueOf(bianhao) == 1){
                continue;
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\汽修宝\\type_detail_original_data\\type_detail_original_data" + bianhao)));
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\type_original_data\\" + filename)));
            String line = null;
            while((line = br.readLine()) != null){
                JSONObject json = JSON.parseObject(line.trim());
                if(json.containsKey("result")){
                    JSONObject result = json.getJSONObject("result");
                    if(result.containsKey("list")){
                        JSONArray list = result.getJSONArray("list");
                        for(int i = 0; i < list.size(); i++){
                            JSONObject data = list.getJSONObject(i);
                            if(data.containsKey("id")){
                                String type_id = data.getString("id");
                                try{
                                    String response = Qixiubao_Online.proxy(type_id);
                                    System.out.println(type_id + "\t" + response);
                                    bw.write(type_id + "\t" + response + "\n");
                                }catch (Exception e){
                                    System.out.println(type_id + "\t" + "[]");
                                    bw.write(type_id + "\t" + "[]" + "\n");
                                }

                            }
                            count = count + 1;
                        }
                    }
                }
            }

            bw.flush();
            bw.close();
        }

        System.out.println(count);

    }
}
