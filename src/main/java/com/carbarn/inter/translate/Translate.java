package com.carbarn.inter.translate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.im.translator.VolcTranslator;

public class Translate {

    public static String translate(VolcTranslator volcTranslator, String data, String sourcelang, String targetLang) {

        for (int i = 0; i < 3; i++) {
            String trans_data = volcTranslator.translate(data, sourcelang, targetLang);
            if (!"".equals(trans_data) && trans_data != null) {
                return trans_data;
            } else {
                continue;
            }
        }

        return "";
    }


    public static String translateTypeDetails(VolcTranslator volcTranslator,
                                              String details,
                                              String sourceLang,
                                              String targetLang) {
        JSONArray json = JSON.parseArray(details);
        for (int i = 0; i < json.size(); i++) {
            JSONObject data = json.getJSONObject(i);
            String key = data.getString("key");
            String trans_key = translate(volcTranslator, key, sourceLang, targetLang);
            data.put("key", trans_key);

            JSONArray params_in = data.getJSONArray("params");
            for (int j = 0; j < params_in.size(); j++) {
                JSONObject data_in = params_in.getJSONObject(j);
                String name = data_in.getString("name");
                String value = data_in.getString("value");
                String trans_name = translate(volcTranslator, name, sourceLang, targetLang);
                String trans_value = translate(volcTranslator, value, sourceLang, targetLang);
                data_in.put("name", trans_name);
                data_in.put("value", trans_value);

            }
        }

        return json.toJSONString();
    }
}
