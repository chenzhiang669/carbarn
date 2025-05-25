package com.carbarn.inter.utils.qixiubao.update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.im.translator.VolcTranslator;
import com.carbarn.inter.translate.Translate;

import java.io.*;
import java.util.*;

public class DetailsKeyValue {
    public static void main(String[] args) throws IOException {
        LocalRunForbidenLog4j.forbidenlog();
        File dir = new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details\\");
        String[] files = dir.list();
        List<JSONObject> typedetails = new ArrayList<JSONObject>();
        Set<String> keywords = new HashSet<String>();
        Set<String> keys = new HashSet<String>();
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
                    String key = data.getString("key");
                    keys.add(key);
                    for(int j = 0; j < params_in.size(); j++) {
                        JSONObject data1 = params_in.getJSONObject(j);
                        String name = data1.getString("name");
                        keywords.add(name);
//                        if(!"车型名称".equals(name)){
//                            String value = data1.getString("value");
//                            keywords.add(value);
//                        }

                    }

                }
            }
        }


        VolcTranslator volcTranslator = new VolcTranslator();
        volcTranslator.init("AKLTMDY3MGI0YzI0Zjg0NDdiODkzZDJjNGQ1ODZhZDMzZGE",
                "TXpnd056aGtOV0V3WmpBd05EWmtZV0ptTXpSa1pUQTJNRFUxT1RRNVpqUQ==");
//        volcTranslator.init();
        List<String> languages = new ArrayList<String>();
        languages.add("en");
        languages.add("ru");


        for(String language:languages){
            Map<String, String> key_value = new HashMap<String, String>();
            BufferedReader br1 = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\关键词翻译")));
            String line = null;
            while((line = br1.readLine()) != null){
                try{

                    String[] infos = line.split("\t");
                    String lang = infos[0].trim();
                    String key = infos[1].trim();
                    String value = infos[2].trim();
                    if(!lang.equals(language)){
                        continue;
                    }
                    key_value.put(key, value);

                }catch (Exception e){
                    System.out.println("error: " + line);
                }

            }


            for (String keyword : keywords) {
                String result = Translate.translate(volcTranslator, key_value, keyword,"zh",language);
                System.out.println(keyword + "\t" + result);
            }

            for (String key : keys) {
                String result = Translate.translate(volcTranslator, key_value,  key,"zh",language);
                System.out.println(key+ "\t" + result);
            }
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

    }
}
