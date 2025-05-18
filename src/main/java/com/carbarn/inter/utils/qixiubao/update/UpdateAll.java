package com.carbarn.inter.utils.qixiubao.update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.test.Translator1;
import com.carbarn.inter.utils.qixiubao.QixiubaoHttp;
import com.carbarn.inter.utils.qixiubao.Transform;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateAll {
    public static void main(String[] args) throws IOException, SQLException {
        LocalRunForbidenLog4j.forbidenlog();
        String ip = args[0];
        int port = Integer.valueOf(args[1]);
        String user = args[2];
        String password = args[3];
        String database = args[4];

        Connection connection = JDBCUtils.getConnection(ip, port, user, password, database);
        Map<String, Integer> carbarn_id = JDBCUtils.getFieldIdMap(connection); //获取车出海字段值id
        Map<String, String> qixiubao_carbarn = JDBCUtils.getQixiubao_Carbarn_Map(connection); //获取汽修宝字段值和车出海字段值的映射关系
        Map<Integer, String> online_brands = JDBCUtils.getOnlineBrands(connection); //brand在线表中的品牌id及品牌

//        updateAllBrands();
//        updateAllSeries();
//        updateAllTypes();
//        updateAllTypeDetails();
//        insertBrands(connection, online_brands);
//        insertSeries(connection, online_brands);
        insertTypes(connection, online_brands, qixiubao_carbarn, carbarn_id);
//        insertTypeDetails(connection, online_brands, qixiubao_carbarn, carbarn_id);
        TypeDetailsTranslate();
    }



    private static void TypeDetailsTranslate() throws IOException {
        File dir = new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details\\");
        String[] files = dir.list();
        List<JSONObject> typedetails = new ArrayList<JSONObject>();
        Map<String, String> key_value = new HashMap<String, String>();
        Translator1 translator1 = new Translator1();
        int count = 0;
        for (String file : files) {
            System.out.println("process file: " + file);
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details\\" + file)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details_en\\" + file)));
            String line = null;
            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                count++;
                JSONObject json = JSON.parseObject(line);
                JSONArray params = json.getJSONArray("params");
                for (int i = 0; i < params.size(); i++) {
                    JSONObject data = params.getJSONObject(i);
                    String key = data.getString("key");
                    String trans_key = translate(translator1, key, "zh", "en", key_value);
                    data.put("key", trans_key);

                    JSONArray params_in = data.getJSONArray("params");
                    for (int j = 0; j < params_in.size(); j++) {
                        JSONObject data_in = params_in.getJSONObject(j);
                        String name = data_in.getString("name");
                        String value = data_in.getString("value");
                        String trans_name = translate(translator1, name, "zh", "en", key_value);
                        String trans_value = translate(translator1, value, "zh", "en", key_value);
                        data_in.put("name", trans_name);
                        data_in.put("value", trans_value);

                    }

                }


                bw.write(json.toJSONString() + "\n");
                if (count % 1000 == 0) {
                    System.out.println("" + count + "datas flush to file" + file);
                    bw.flush();
                }
            }

            System.out.println("" + count + "datas flush to file" + file);
            bw.flush();

        }
    }

    public static String translate(Translator1 translator1, String data, String sourcelang, String targetLang, Map<String, String> key_value) {
        if (key_value.containsKey(data)) {
            return key_value.get(data);
        } else {

            for (int i = 0; i < 3; i++) {
                String trans_data = translator1.translate(data, sourcelang, targetLang);
                if ("".equals(trans_data) || trans_data == null) {
                    continue;
                } else {
                    key_value.put(data, trans_data);
                    return trans_data;
                }
            }

            return data;
        }
    }

    private static void insertTypeDetails(Connection connection,
                                          Map<Integer, String> online_brands,
                                          Map<String, String> qixiubao_carbarn,
                                          Map<String, Integer> carbarn_id) throws IOException, SQLException {

        File dir = new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details\\");
        String[] files = dir.list();
        List<JSONObject> typedetails = new ArrayList<JSONObject>();
        for (String file : files) {
            System.out.println("process file: " + file);
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details\\" + file)));
            String line = null;
            while ((line = br.readLine()) != null) {
                JSONObject json = JSON.parseObject(line);
                typedetails.add(json);

                if (typedetails.size() % 1000 == 0) {
                    System.out.println("update " + typedetails.size() + " details");
                    JDBCUtils.updateTypeDetails(connection, typedetails, online_brands, "zh");
                    typedetails = new ArrayList<JSONObject>();
                }
            }
        }

        System.out.println("update " + typedetails.size() + " details");
        JDBCUtils.updateTypeDetails(connection, typedetails, online_brands, "zh");
    }

    private static void insertTypes(Connection connection,
                                    Map<Integer, String> online_brands,
                                    Map<String, String> qixiubao_carbarn,
                                    Map<String, Integer> carbarn_id) throws IOException, SQLException {
        File dir = new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\types\\");
        String[] files = dir.list();
//        int count = 0;
        List<String> typeinfos = new ArrayList<String>();
        for (String file : files) {
            String file_count = file.split("_")[1];
            System.out.println(file);
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\types\\" + file)));
            String line = null;

            while ((line = br.readLine()) != null) {
                typeinfos.add(line.trim());

                if (typeinfos.size() % 4 == 0) {
                    System.out.println("insert " + typeinfos.size() + " brands");
                    List<JSONObject> typeinfo_transform = Transform.typeInfoTransform(typeinfos, qixiubao_carbarn, carbarn_id);
                    JDBCUtils.insertNewType(connection, typeinfo_transform, online_brands, "ru");
                    typeinfos = new ArrayList<String>();
                }
            }
        }

        System.out.println("insert " + typeinfos.size() + " brands");
        List<JSONObject> typeinfo_transform = Transform.typeInfoTransform(typeinfos, qixiubao_carbarn, carbarn_id);
        JDBCUtils.insertNewType(connection, typeinfo_transform, online_brands, "ru");

    }

    private static void insertSeries(Connection connection, Map<Integer, String> online_brands) throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\series")));
        String line = null;
        int count = 0;
        List<JSONObject> series = new ArrayList<JSONObject>();
        Translator1 translator = new Translator1();
        Map<String, String> key_value = new HashMap<String, String>();
        while ((line = br.readLine()) != null) {
            JSONObject json = JSON.parseObject(line.trim());
            if (json.containsKey("result")) {
                JSONArray result = json.getJSONArray("result");
                for (int i = 0; i < result.size(); i++) {
                    JSONObject data = result.getJSONObject(i);

                    data.put("series_id", data.getInteger("id"));
                    String name = data.getString("name");
                    String trans_name = translate(translator, name,"zh","ru",key_value);
                    String first_char = trans_name.substring(0,1);
                    System.out.println(trans_name);
                    System.out.println(first_char);

                    data.put("series", trans_name);
                    data.put("first_char", first_char);
                    data.put("brand_id", data.getInteger("brand_id"));
                    series.add(data);

                    if (series.size() % 100 == 0) {
                        System.out.println("insert " + series.size() + " brands");
                        JDBCUtils.insertNewSeries(connection, series, online_brands, "ru");
                        series = new ArrayList<JSONObject>();
                    }
                }
            }
        }

        System.out.println("insert " + series.size() + " brands");
        JDBCUtils.insertNewSeries(connection, series, online_brands, "ru");
    }

    private static void insertBrands(Connection connection,
                                     Map<Integer, String> online_brands) throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\brands")));
        String line = null;
        int count = 0;
        Translator1 translator = new Translator1();
        Map<String, String> key_value = new HashMap<String, String>();
        while ((line = br.readLine()) != null) {

            List<JSONObject> insert_datas = new ArrayList<JSONObject>();
            JSONObject json = JSON.parseObject(line.trim());
            if (json.containsKey("result")) {
                JSONArray result = json.getJSONArray("result");
                for (int i = 0; i < result.size(); i++) {
                    JSONObject data = result.getJSONObject(i);
                    data.put("logo", data.getString("img_url"));
//                    data.put("first_char", data.getString("first_letter"));
                    String name = data.getString("name");
                    String trans_name = translate(translator,name, "zh", "ru", key_value);
                    String first_char = trans_name.substring(0,1);
                    System.out.println(trans_name);
                    System.out.println(first_char);
                    data.put("first_char", first_char);
                    data.put("brand", trans_name);
                    data.put("brand_id", data.getInteger("id"));
                    insert_datas.add(data);

                    if (insert_datas.size() % 100 == 0) {
                        System.out.println("insert " + insert_datas.size() + " brands");
                        JDBCUtils.insertNewBrand(connection, insert_datas, online_brands, "ru");
                        insert_datas = new ArrayList<JSONObject>();
                    }
                }

                System.out.println("insert " + insert_datas.size() + " brands");
                JDBCUtils.insertNewBrand(connection, insert_datas, online_brands, "ru");
                insert_datas = new ArrayList<JSONObject>();
            }
        }
    }

    private static void updateAllTypeDetails() throws IOException {

        File dir = new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\types\\");
        String[] files = dir.list();
        int count = 0;
        for (String file : files) {
            String file_count = file.split("_")[1];
            BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\types\\" + file)));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\type_details\\type_details_" + file_count)));
            String line = null;

            while ((line = br.readLine()) != null) {
                JSONObject json = JSON.parseObject(line.trim());
                if (!json.containsKey("result")) {
                    continue;
                }

                JSONObject result = json.getJSONObject("result");
                if (!result.containsKey("list")) {
                    continue;
                }

                JSONArray list = result.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    count++;
                    JSONObject data = list.getJSONObject(i);
                    int type_id = data.getInteger("id");
                    JSONObject detail = QixiubaoHttp.searchTypeDetails(String.valueOf(type_id));
                    if (detail == null) {
                        continue;
                    }
                    bw.write(detail.toJSONString() + "\n");
                    if (count % 50 == 0) {
                        System.out.println("file_count: " + file_count + " typedetails: count = " + count);
                        bw.flush();
                    }
                }

                System.out.println("file_count: " + file_count + " typedetails: count = " + count);
                bw.flush();

            }
        }
    }

    private static void updateAllTypes() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\series")));
        String line = null;
        int file_count = 0;
        int count = 0;
        while ((line = br.readLine()) != null) {
            file_count++;
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\types\\types_" + file_count)));
            JSONObject json = JSON.parseObject(line.trim());
            if (json.containsKey("result")) {
                JSONArray result = json.getJSONArray("result");
                for (int i = 0; i < result.size(); i++) {
                    count++;
                    JSONObject data = result.getJSONObject(i);
                    int brand_id = data.getInteger("brand_id");
                    int series_id = data.getInteger("id");
                    List<String> types = QixiubaoHttp.searcTypes(String.valueOf(brand_id), String.valueOf(series_id));

                    for (String type : types) {
                        bw.write(type + "\n");
                    }

                    if (count % 50 == 0) {
                        System.out.println("brand_id: " + brand_id + " types: count = " + count);
                        bw.flush();
                    }

                }

                System.out.println("types: count = " + count);
                bw.flush();
            }
        }


    }

    private static void updateAllSeries() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\brands")));
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\series")));
        String line = null;
        int count = 0;
        while ((line = br.readLine()) != null) {
            JSONObject json = JSON.parseObject(line.trim());
            if (json.containsKey("result")) {
                JSONArray result = json.getJSONArray("result");
                for (int i = 0; i < result.size(); i++) {
                    count++;
                    JSONObject data = result.getJSONObject(i);
                    int brand_id = data.getInteger("id");
                    String series = QixiubaoHttp.searchSeries(String.valueOf(brand_id));
                    bw.write(series + "\n");
                    if (count % 50 == 0) {
                        System.out.println("series: count = " + count);
                        bw.flush();
                    }
                }
            }
        }
        System.out.println("series: count = " + count);
        bw.flush();
    }

    private static void updateAllBrands() throws IOException {
        String brands = QixiubaoHttp.searchAllBrands();
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\carbarn\\汽修宝\\update_all_2025-05-10\\brands")));
        bw.write(brands + "\n");
        bw.flush();
    }
}
