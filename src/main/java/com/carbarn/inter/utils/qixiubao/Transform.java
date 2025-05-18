package com.carbarn.inter.utils.qixiubao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.test.Translator1;
import com.carbarn.inter.utils.qixiubao.update.UpdateAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transform {

    private static final Logger logger = LoggerFactory.getLogger(Transform.class);

    public static final Translator1 translate = new Translator1();

    public static int getFieldId(JSONObject data,
                                 String key_qixiubao,
                                 String field_carbarn,
                                 String default_carbarn_value,
                                 Map<String, String> qixiubao_carbarn,
                                 Map<String, Integer> carbarn_id) {
        int id = -1;
        try {
            String value_qixiubao = data.getString(key_qixiubao);
            if (qixiubao_carbarn.containsKey(value_qixiubao)) {
                String carbarn_value = qixiubao_carbarn.get(value_qixiubao);
                String key_carbarn = field_carbarn + "_" + carbarn_value;
                if (carbarn_id.containsKey(key_carbarn)) {
                    id = carbarn_id.get(key_carbarn);
                }
            }
        } catch (Exception e) {
            id = carbarn_id.get(field_carbarn + "_" + default_carbarn_value);
        }

        if (id == -1) {
            id = carbarn_id.get(field_carbarn + "_" + default_carbarn_value);
        }

        return id;
    }


    //车型信息转化为车出海车型信息()
    public static List<JSONObject> typeInfoTransform(List<String> typeinfos,
                                                     Map<String, String> qixiubao_carbarn,
                                                     Map<String, Integer> carbarn_id) {

        List<JSONObject> transforms = new ArrayList<JSONObject>();

        for (String line : typeinfos) {
            JSONObject jsonObject = JSON.parseObject(line.trim());
            if (!jsonObject.containsKey("result")) {
                continue;
            }

            JSONObject result = jsonObject.getJSONObject("result");
            if (!result.containsKey("list")) {
                continue;
            }
            JSONArray list = result.getJSONArray("list");

            for (int i = 0; i < list.size(); i++) {
                JSONObject transform = new JSONObject();

                JSONObject data = list.getJSONObject(i);

                String type = data.getString("description");
                transform.put("type", type);

                int type_id = data.getInteger("id");
                transform.put("type_id", type_id);

                String series = data.getString("series");
                transform.put("series", series);

                int series_id = data.getInteger("series_id");
                transform.put("series_id", series_id);

                String brand = data.getString("brand");
                transform.put("brand", brand);

                int brand_id = data.getInteger("brand_id");
                transform.put("brand_id", brand_id);

                String year = data.getString("years");
                transform.put("year", year);

                String language = "zh";
                transform.put("language", language);

                double price = 0.0;
                transform.put("price", price);

                double floor_price = 0.0;
                transform.put("floor_price", floor_price);

                double guide_price = 0.0;
                try {
                    guide_price = Double.valueOf(data.getString("price").replaceAll("万", "")) * 10000;
                } catch (Exception e) {
                    guide_price = 0.0;
                }
                transform.put("guide_price", guide_price);

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
                transform.put("displacement", displacement);
                transform.put("displacement_type", displacement_type);


                double power = 0.0;
                try {
                    power = Double.valueOf(data.getString("max_power"));
                } catch (Exception e) {
                    power = 0.0;
                }
                transform.put("power", power);

                double battery_capacity = 0.0;
                transform.put("battery_capacity", battery_capacity);

                double pure_electric_range = 0.0;
                transform.put("pure_electric_range", pure_electric_range);

                int color = -1;
                transform.put("color", color);

                int transmission = getFieldId(data, "trans_des", "transmission", "自动", qixiubao_carbarn, carbarn_id);
                transform.put("transmission", transmission);

                int emission_standards = getFieldId(data, "environmental_standards", "emission_standards", "其他", qixiubao_carbarn, carbarn_id);
                transform.put("emission_standards", emission_standards);


                int type_of_manu = getFieldId(data, "djit", "type_of_manu", "国产", qixiubao_carbarn, carbarn_id);
                transform.put("type_of_manu", type_of_manu);

                String _engine = data.getString("engine");
                if ("纯电动".equals(_engine)) {
                    int engine = carbarn_id.get("engine_电动");
                    transform.put("engine", engine);
                } else {
                    int engine = getFieldId(data, "fuel_type", "engine", "其他", qixiubao_carbarn, carbarn_id);
                    transform.put("engine", engine);
                }


                int drive_type = getFieldId(data, "driver", "drive_type", "未知", qixiubao_carbarn, carbarn_id);
                transform.put("drive_type", drive_type);

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
                transform.put("box", box);


                int type_of_car = getFieldId(data, "level", "type_of_car", "不限", qixiubao_carbarn, carbarn_id);
                transform.put("type_of_car", type_of_car);

                String seat_capacity_string = data.getString("seats");
                if (seat_capacity_string == null || "".equals(seat_capacity_string.trim())) {
                    seat_capacity_string = "0";
                }

                int seat_capacity = 0;
                try {
                    seat_capacity = Integer.valueOf(seat_capacity_string);
                } catch (Exception e) {
                    seat_capacity = 0;
                }
                transform.put("seat_capacity", seat_capacity);
                transforms.add(transform);

            }
        }

        return transforms;

    }

    //车型详细信息转化为车出海车型详细信息
    public static JSONObject typeDetailsTransform(String type_detail,
                                                  String type_id) {

        JSONObject data = new JSONObject();
        data.put("type_id", type_id);
        String _line = type_detail;
        JSONObject jsonObject = JSON.parseObject(_line.trim());
        if (!jsonObject.containsKey("result")) {
            return null;
        }
        JSONObject result = jsonObject.getJSONObject("result");

        if (!result.containsKey("params")) {
            return null;
        }
        JSONArray params = result.getJSONArray("params");

        JSONArray my_result = new JSONArray();
        for (int i = 0; i < params.size(); i++) {
            JSONObject a = params.getJSONObject(i);
            String my_key = a.getString("type");
            JSONArray items = a.getJSONArray("items");

            JSONArray my_params = new JSONArray();

            for (int j = 0; j < items.size(); j++) {
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

        data.put("params", my_result);
        return data;

    }


    //车型详细信息转化为车出海车型详细信息
    public static JSONObject getBatteryInfosFromDetails(String type_detail) {
        JSONObject json = new JSONObject();
        double battery_capacity = 0.0;
        double pure_electric_range = 0.0;
        JSONArray arrays = (JSONArray) JSONArray.parse(type_detail);
        for (int i = 0; i < arrays.size(); i++) {
            JSONObject a = arrays.getJSONObject(i);
            String key = a.getString("key");
            JSONArray params = a.getJSONArray("params");

            for (int j = 0; j < params.size(); j++) {
                JSONObject datas = params.getJSONObject(j);
                String name = datas.getString("name");
                String value = datas.getString("value");
                try {
                    if (name.contains("电池能量(kWh)") && value != null) {
                        battery_capacity = Double.valueOf(value);
                    } else if (name.contains("续航") && value != null && Double.valueOf(value) > pure_electric_range) {
                        pure_electric_range = Double.valueOf(value);
                    }
                } catch (Exception e) {
                    battery_capacity = 0.0;
                    pure_electric_range = 0.0;
                }
            }
        }


        json.put("battery_capacity", battery_capacity);
        json.put("pure_electric_range", pure_electric_range);

        return json;
    }


    //解析通过vin获取到的数据信息，并映射为车出海的字段值
    public static JSONObject standard_data(JSONObject json) {
        JSONObject data = new JSONObject();
        String model_name = (String) json.getOrDefault("model_name", "");
        String series = (String) json.getOrDefault("series", "");
        String brand = (String) json.getOrDefault("brand", "");
        if ("".equals(model_name) || "".equals(series) || "".equals(brand)) {
            return data;
        }
        String type = series + " " + model_name;
        data.put("type", type);
        data.put("series", series);
        data.put("brand", brand);
        data.put("year", json.getOrDefault("years", null));
        data.put("language", "zh");
        data.put("price", 0.0);
        data.put("floor_price", 0.0);
        data.put("color", "其他");
        data.put("power", 0.0);
        data.put("battery_capacity", 0.0);
        data.put("pure_electric_range", 0.0);

        try {
            if (json.containsKey("seat")) {
                String seat = json.getString("seat");
                if (seat != null && !"".equals(seat)) {
                    data.put("seat_capacity", Integer.valueOf(seat));
                }
            }
        } catch (Exception e) {
            data.put("seat_capacity", 0);
        }

        try {
            if (json.containsKey("price")) {
                String price = json.getString("price");
                if (price.contains("万")) {
                    double guide_price = Double.valueOf(price.replace("万", "")) * 10000;
                    data.put("guide_price", guide_price);  //TODO
                }
            }
        } catch (Exception e) {
            data.put("guide_price", 0.0);
        }

        try {
            if (json.containsKey("engine")) {
                String engine = json.getString("engine");
                if (engine.contains("T")) {
                    data.put("displacement_type", "T");
                    double displacement = Double.valueOf(engine.replaceAll("T", ""));
                    data.put("displacement", displacement);
                } else if (engine.contains("L")) {
                    data.put("displacement_type", "L");
                    double displacement = Double.valueOf(engine.replaceAll("L", ""));
                    data.put("displacement", displacement);
                } else {
                    data.put("displacement", 0.0);
                    data.put("displacement_type", "T");
                }
            }
        } catch (Exception e) {
            data.put("displacement", 0.0);
            data.put("displacement_type", "T");
        }


        if (json.containsKey("level")) {
            String level = Contants.qixiubao_carbarn.getOrDefault(json.getString("level"), "不限");
            data.put("type_of_car", level);
        }

        if (json.containsKey("trans_des")) {
            String trans_des = json.getString("trans_des");
            String transmission = Contants.qixiubao_carbarn.getOrDefault(trans_des, "自动");
            data.put("transmission", transmission);
        }

        if (json.containsKey("emission_standard")) {
            String emission_standard = Contants.qixiubao_carbarn.getOrDefault(json.getString("emission_standard"), "其他");
            data.put("emission_standards", emission_standard);
        }

        data.put("type_of_manu", "国产");

        if ("纯电动".equals(json.getString("engine"))) {
            data.put("engine", "电动");
        } else if (json.containsKey("fuel_type")) {
            String fuel_type = Contants.qixiubao_carbarn.getOrDefault(json.getString("fuel_type"), "其他");
            data.put("engine", fuel_type);
        }

        if (json.containsKey("driver_type")) {
            String driver_type = Contants.qixiubao_carbarn.getOrDefault(json.getString("driver_type"), "未知");
            data.put("drive_type", driver_type);
//            String driver_type = json.getString("driver_type");
//            if(driver_type.contains("前驱")){
//                data.put("drive_type", "前驱");
//            }else if(driver_type.contains("后驱")){
//                data.put("drive_type", "后驱");
//            }else if(driver_type.contains("四驱")){
//                data.put("drive_type", "四驱");
//            }else{
//                data.put("drive_type", "未知");
//            }

        }

        if (json.containsKey("body")) {
            String body = json.getString("body");
            if (body.contains("单厢")) {
                data.put("box", "单厢");
            } else if (body.contains("两厢")) {
                data.put("box", "两厢");
            } else if (body.contains("三厢")) {
                data.put("box", "三厢");
            } else {
                data.put("box", "未知");
            }
        }

        return data;
    }
}
