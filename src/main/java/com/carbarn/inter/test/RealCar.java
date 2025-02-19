package com.carbarn.inter.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.mapper.CarsMapper;
import com.carbarn.inter.pojo.CarsPOJO;
import com.carbarn.inter.utils.Utils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class RealCar {

    public static String base_url = "https://img.jytche.com/";
    public static Map<String, Integer> get_index_map() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/carbarn?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&allowMultiQueries=true";
        String username = "root";
        String password = "123456";

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "select * from carbarn.`index` where `language`= 'zh' and is_mapping = 0";
        ResultSet resultSet = statement.executeQuery(sql);

        Map<String, Integer> index_map = new HashMap<String, Integer>();
        while (resultSet.next()) {
            String value = resultSet.getString("value");
            int value_id = resultSet.getInt("value_id");
            // 处理其他字段
            index_map.put(value, value_id);
        }


        return index_map;
    }

    public static Map<String, ArrayList<Integer>> get_index_map1() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/carbarn?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&allowMultiQueries=true";
        String username = "root";
        String password = "123456";

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "select * from carbarn.`index` where `language`= 'zh' and is_mapping = 0";
        ResultSet resultSet = statement.executeQuery(sql);

        Map<String, ArrayList<Integer>> index_map = new HashMap<String, ArrayList<Integer>>();
        while (resultSet.next()) {
            String field = resultSet.getString("field");
            int value_id = resultSet.getInt("value_id");

            if(!index_map.containsKey(field)){
                ArrayList<Integer> list = new ArrayList<Integer>();
                list.add(value_id);
                index_map.put(field, list);
            }else{
                index_map.get(field).add(value_id);
            }
        }


        return index_map;
    }

    public static Map<String, ArrayList<Integer>> getbrand() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/carbarn?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&allowMultiQueries=true";
        String username = "root";
        String password = "123456";

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "select * from carbarn.car_type where `language`= 'zh'";
        ResultSet resultSet = statement.executeQuery(sql);

        Map<String, ArrayList<Integer>> index_map = new HashMap<String, ArrayList<Integer>>();
        while (resultSet.next()) {
            String type = resultSet.getString("type");
            int index = type.indexOf("(");
            if(index > 0){
                type = type.substring(0, index);
            }
            int brand_id = resultSet.getInt("brand_id");
            int series_id = resultSet.getInt("series_id");
            int type_id = resultSet.getInt("type_id");

            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(brand_id);
            list.add(series_id);
            list.add(type_id);

            // 处理其他字段
            index_map.put(type, list);
        }


        return index_map;


    }

    public static void car_details() throws IOException, SQLException, ClassNotFoundException {

        SqlSessionFactory factory = SqlSessionFactoryUtil.sqlSessionFactory;
        // true 不开启事务，自动提交
        SqlSession session = factory.openSession();
        CarsMapper mapper = session.getMapper(CarsMapper.class);

        Map<String, Integer> index_map = get_index_map();
        Map<String, ArrayList<Integer>> brand_ids = getbrand();

        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\car_detail.json")));
        String line = null;
        while((line = br.readLine()) != null){
//            System.out.println(line);
            JSONObject json = JSON.parseObject(line.trim());

            CarsPOJO carsPOJO = new CarsPOJO();
            carsPOJO.setUser_id(1);

            String name = json.getString("name");
            int index = name.indexOf("款");
            String type = name.substring(index - 4);


            int brand_id = brand_ids.get(type).get(0);
            int series_id = brand_ids.get(type).get(1);
            int type_id = brand_ids.get(type).get(2);
            carsPOJO.setBrand_id(brand_id);
            carsPOJO.setSeries_id(series_id);
            carsPOJO.setType_id(type_id);

            String color = json.getString("out_color_label");
            int color_id = index_map.getOrDefault(color, -1);
            carsPOJO.setColor(color_id);

            String engine = json.getJSONObject("sub").getString("fuel_type_label");
            int engine_id = index_map.getOrDefault(engine, -1);
            carsPOJO.setEngine(engine_id);

            String transmission = json.getString("gearbox_label");
            int transmission_id = index_map.getOrDefault(transmission, -1);
            carsPOJO.setTransmission(transmission_id);

            String emission_standards = json.getString("emission_label");
            int emission_standards_id = index_map.getOrDefault(emission_standards, -1);
            carsPOJO.setEmission_standards(emission_standards_id);
            String type_of_manu = json.getString("mum_type_label");
            int type_of_manu_id = index_map.getOrDefault(type_of_manu, -1);
            carsPOJO.setType_of_manu(type_of_manu_id);
            String type_of_car = json.getString("category_type_label");
            int type_of_car_id = index_map.getOrDefault(type_of_car, -1);
            carsPOJO.setType_of_car(type_of_car_id);
//            System.out.println(type + "\t" + brand_id + "\t" + series_id +"\t" + type_id
//                    +"\t" + color_id + "\t" +engine_id + "\t" + transmission_id+ "\t" +emission_standards_id + "\t" +type_of_manu_id + "\t" +type_of_car_id);

            int origin_country_id = -1;
            carsPOJO.setOrigin_country(origin_country_id);

            int drive_type_id = -1;
            carsPOJO.setDrive_type(drive_type_id);

            int box_id = -1;
            carsPOJO.setBox(box_id);

            String displacement_ori = json.getString("capacity_label");
            if(displacement_ori.contains("T")){
                double displacement = Double.valueOf(displacement_ori.replaceAll("T",""));
                String displacement_type = "T";
                carsPOJO.setDisplacement(displacement);
                carsPOJO.setDisplacement_type(displacement_type);
            }else if(displacement_ori.contains("L")){
                double displacement = Double.valueOf(displacement_ori.replaceAll("L",""));
                String displacement_type = "L";
                carsPOJO.setDisplacement(displacement);
                carsPOJO.setDisplacement_type(displacement_type);
            }

            int seat_capacity = 0;
            carsPOJO.setSeat_capacity(seat_capacity);
//            double new_car_price = Double.valueOf(json.getString("guide_price_label").replaceAll("万","")) * 10000;
//            carsPOJO.set
            String ctali_date = "2025-12-25";
            carsPOJO.setCtali_date(ctali_date);
            String avi_date = "2025-12-25";
            carsPOJO.setAvi_date(avi_date);
            double car_age = -1;
            carsPOJO.setCar_age(car_age);
            double mileage = Double.valueOf(json.getString("mileage_label").replaceAll("万公里","")) * 10000;
            carsPOJO.setMileage(mileage);
            String vin = Utils.getRandomChar(16);
            carsPOJO.setVin(vin);
            String manufacture_date = json.getString("plate_date");
            carsPOJO.setManufacture_date(manufacture_date);
            String plate_issue_date = json.getString("plate_date");
            carsPOJO.setPlate_issue_date(plate_issue_date);
            double price = json.getDouble("price");
            carsPOJO.setPrice(price);
            double floor_price = json.getDouble("bottom_price");
            carsPOJO.setFloor_price(floor_price);
            double guide_price = json.getDouble("bottom_price") - 10000;
            carsPOJO.setGuide_price(guide_price);
            double power = 0.0;
            carsPOJO.setPower(power);
            double battery_capacity = 0.0;
            carsPOJO.setBattery_capacity(battery_capacity);
            double pure_electric_range = 0.0;
            carsPOJO.setPure_electric_range(pure_electric_range);
            String city = json.getString("city_label");
            carsPOJO.setCity(city);
            int is_deal = 0;
            carsPOJO.setIs_deal(is_deal);
            int is_lock = 0;
            carsPOJO.setIs_lock(is_lock);
            int state = 0;
            carsPOJO.setState(state);
            String description = json.getString("description");
            carsPOJO.setDescription(description);
            JSONArray images = json.getJSONArray("images");
            String header_picture = "";
            ArrayList<String> all_pictures = new ArrayList<String>();

            for(int i = 0; i< images.size(); i++){
                JSONObject image = images.getJSONObject(i);
                if(i == 0){
                    header_picture = base_url + image.getString("filename");
                    carsPOJO.setHeader_picture(header_picture);
                }else{
                    all_pictures.add(base_url + image.getString("filename"));
                    carsPOJO.setAll_pictures(String.join(",", all_pictures));
                }
            }

            String proof = null;
            carsPOJO.setProof(proof);

            System.out.println(header_picture + "\t" + String.join(",", all_pictures));

            List<Integer> label = new ArrayList<Integer>();
            label.add(-1);
            carsPOJO.setLabel(label);
            String inspection_report = "";
            carsPOJO.setInspection_report(inspection_report);
            int car_condition = -1;
            carsPOJO.setCar_condition(car_condition);
            int coating = -1;
            carsPOJO.setCoating(coating);
            int component = -1;
            carsPOJO.setComponent(component);
            int engine_condition = -1;
            carsPOJO.setEngine_condition(engine_condition);
            int transmission_condition = -1;
            carsPOJO.setTransmission_condition(transmission_condition);
            int number_of_transfers = -1;
            carsPOJO.setNumber_of_transfers(number_of_transfers);
            int mileage_contition = -1;
            carsPOJO.setMileage_contition(mileage_contition);
            String car_condition_desc = "";
            carsPOJO.setCar_condition_desc(car_condition_desc);

            System.out.println(carsPOJO.toString());
            mapper.insertNewCar(carsPOJO);
            session.commit();

        }

        session.close();
    }


    public static void car_code() throws IOException, SQLException, ClassNotFoundException {

        SqlSessionFactory factory = SqlSessionFactoryUtil.sqlSessionFactory;
        // true 不开启事务，自动提交
        SqlSession session = factory.openSession();
        CarsMapper mapper = session.getMapper(CarsMapper.class);

        Random random = new Random();

        Map<String, ArrayList<Integer>> index_map = get_index_map1();
        Map<String, ArrayList<Integer>> brand_ids = getbrand();

        BufferedReader br = new BufferedReader(new FileReader(new File("D:\\carbarn\\car_code.json")));
        String line = null;
        int id = 0;
        int unvalid = 0;
        int count = 0;
        while ((line = br.readLine()) != null) {
            JSONObject jsons = JSON.parseObject(line.trim());
            JSONArray list = jsons.getJSONArray("lists");
            for(int i = 0; i < list.size(); i++){
                CarsPOJO carsPOJO = new CarsPOJO();
                JSONObject json = list.getJSONObject(i);
                carsPOJO.setUser_id(1);

                String name = json.getString("name");
                int index = name.indexOf("款");
                String type = name;
                if(index > 0){
                    type = name.substring(index - 4);
                }


                if(!brand_ids.containsKey(type)){
                    unvalid++;
                    continue;
                }

                count++;

                int brand_id = brand_ids.get(type).get(0);
                int series_id = brand_ids.get(type).get(1);
                int type_id = brand_ids.get(type).get(2);
                carsPOJO.setBrand_id(brand_id);
                carsPOJO.setSeries_id(series_id);
                carsPOJO.setType_id(type_id);

                ArrayList<Integer> ids = index_map.get("color");
                int color_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setColor(color_id);

                ids = index_map.get("engine");
                int engine_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setEngine(engine_id);

                ids = index_map.get("transmission");
                int transmission_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setTransmission(transmission_id);

                ids = index_map.get("emission_standards");
                int emission_standards_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setEmission_standards(emission_standards_id);

                ids = index_map.get("type_of_manu");
                int type_of_manu_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setType_of_manu(type_of_manu_id);


                ids = index_map.get("type_of_car");
                int type_of_car_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setType_of_car(type_of_car_id);

                ids = index_map.get("origin_country");
                int origin_country_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setOrigin_country(origin_country_id);

                ids = index_map.get("drive_type");
                int drive_type_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setDrive_type(drive_type_id);

                ids = index_map.get("box");
                int box_id = ids.get(random.nextInt(ids.size()));
                carsPOJO.setBox(box_id);

                double displacement = random.nextInt(4);
                String displacement_type = "T";
                carsPOJO.setDisplacement(displacement);
                carsPOJO.setDisplacement_type(displacement_type);

                int seat_capacity = random.nextInt(7);
                carsPOJO.setSeat_capacity(seat_capacity);
//            double new_car_price = Double.valueOf(json.getString("guide_price_label").replaceAll("万","")) * 10000;
//            carsPOJO.set
                String ctali_date = "2025-12-25";
                carsPOJO.setCtali_date(ctali_date);
                String avi_date = "2025-12-25";
                carsPOJO.setAvi_date(avi_date);
                double car_age = -1;
                carsPOJO.setCar_age(car_age);
                double mileage = Double.valueOf(json.getString("mileage_label").replaceAll("万公里","")) * 10000;
                carsPOJO.setMileage(mileage);
                String vin = Utils.getRandomChar(16).toUpperCase();
                carsPOJO.setVin(vin);
                String manufacture_date = json.getString("plate_date_label");
                carsPOJO.setManufacture_date(manufacture_date);
                String plate_issue_date = json.getString("plate_date_label");
                carsPOJO.setPlate_issue_date(plate_issue_date);
                double price = Double.valueOf(json.getString("price_label").replaceAll("万","")) * 10000;
                carsPOJO.setPrice(price);
                double floor_price = Double.valueOf(json.getString("price_label").replaceAll("万","")) * 10000;
                carsPOJO.setFloor_price(floor_price);
                double guide_price = Double.valueOf(json.getString("price_label").replaceAll("万","")) * 10000 - 10000;
                carsPOJO.setGuide_price(guide_price);
                double power = 0.0;
                carsPOJO.setPower(power);
                double battery_capacity = 0.0;
                carsPOJO.setBattery_capacity(battery_capacity);
                double pure_electric_range = 0.0;
                carsPOJO.setPure_electric_range(pure_electric_range);
                String city = json.getString("city_label");
                carsPOJO.setCity(city);
                int is_deal = 0;
                carsPOJO.setIs_deal(is_deal);
                int is_lock = 0;
                carsPOJO.setIs_lock(is_lock);
                int state = 0;
                carsPOJO.setState(state);
                String description = json.getString("description");
                carsPOJO.setDescription(description);
                String header_picture = base_url + json.getString("image");
                carsPOJO.setHeader_picture(header_picture);
                String all_pictures = base_url + json.getString("image");
                carsPOJO.setAll_pictures(all_pictures);

                String proof = null;
                carsPOJO.setProof(proof);

                System.out.println(header_picture + "\t" + String.join(",", all_pictures));

                List<Integer> label = new ArrayList<Integer>();
                label.add(-1);
                carsPOJO.setLabel(label);
                String inspection_report = "";
                carsPOJO.setInspection_report(inspection_report);
                int car_condition = -1;
                carsPOJO.setCar_condition(car_condition);
                int coating = -1;
                carsPOJO.setCoating(coating);
                int component = -1;
                carsPOJO.setComponent(component);
                int engine_condition = -1;
                carsPOJO.setEngine_condition(engine_condition);
                int transmission_condition = -1;
                carsPOJO.setTransmission_condition(transmission_condition);
                int number_of_transfers = -1;
                carsPOJO.setNumber_of_transfers(number_of_transfers);
                int mileage_contition = -1;
                carsPOJO.setMileage_contition(mileage_contition);
                String car_condition_desc = "";
                carsPOJO.setCar_condition_desc(car_condition_desc);
                System.out.println(carsPOJO);

                mapper.insertNewCar(carsPOJO);
                session.commit();
            }
        }

        System.out.println(unvalid);
        System.out.println(count);
        session.close();
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        car_code();
    }
}
