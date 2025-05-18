package com.carbarn.inter.utils.qixiubao.update;

import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.utils.Utils;
import com.carbarn.inter.utils.qixiubao.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

public class JDBCUtils {

    private static final Logger logger = LoggerFactory.getLogger(JDBCUtils.class);
    public static final String jdbc_url_format = "jdbc:mysql://%s:%s/%s";

    public static Connection getConnection(String ip,
                                           int port,
                                           String user,
                                           String password,
                                           String db) throws SQLException {
        String url = String.format(jdbc_url_format, ip, port, db);
        Connection conn = DriverManager.getConnection(url, user, password);

        return conn;
    }


    //获取车出海字段值--车出海id映射关系
    public static Map<String, Integer> getFieldIdMap(Connection conn){
        Map<String, Integer>  carbarn_id = new HashMap<String,Integer>();
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from carbarn.`index` where `language` = 'zh'");
            while(resultSet.next()){
                int value_id = resultSet.getInt("value_id");
                String value = resultSet.getString("value");
                String field = resultSet.getString("field");

                carbarn_id.put(field + "_" + value, value_id);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return carbarn_id;
    }

    //汽修宝和车出海车型字段信息映射关系获取
    public static Map<String, String> getQixiubao_Carbarn_Map(Connection conn) {

        Map<String, String> qixiubao_carbarn = new HashMap<String,String>();
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from carbarn.qixiubao_carbarn");
            while(resultSet.next()){
                String qixiubao = resultSet.getString("qixiubao");
                String carbarn = resultSet.getString("carbarn");

                qixiubao_carbarn.put(qixiubao, carbarn);
            }


        }catch (Exception e){

        }

        return qixiubao_carbarn;
    }


    //获取在线brand的id
    public static Map<Integer, String> getOnlineBrands(Connection conn) {

        Map<Integer, String> online_brands = new HashMap<Integer, String>();
        try{

            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from carbarn.car_brand where language = 'zh'");
            while(resultSet.next()){
                int brand_id = resultSet.getInt("brand_id");
                String brand = resultSet.getString("brand");

                online_brands.put(brand_id, brand);
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("getOnlineBrands error");
        }

        return online_brands;
    }


    public static void insertNewBrand(Connection conn,
                                      List<JSONObject> typeinfo_transform,
                                      Map<Integer, String> online_brands,
                                      String language) throws SQLException {

        logger.info("---------start to insert new Brands----------------------------");

        String sql_format = "INSERT INTO %s.%s (first_char, brand, brand_id, logo,`language`) \n" +
                "VALUES (?,?,?,?,?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "first_char = ?,\n" +
                "brand = ?, \n" +
                "update_time = NOW()";

        String insert_carbarn_bak_brand_sql = String.format(sql_format, "carbarn_bak", "car_brand_all");
        PreparedStatement pstmt = conn.prepareStatement(insert_carbarn_bak_brand_sql);

        String insert_carbarn_brand_sql = String.format(sql_format, "carbarn", "car_brand");
        PreparedStatement pstmt_online = conn.prepareStatement(insert_carbarn_brand_sql);

        Set<Integer> brand_ids = new HashSet<Integer>();
        int bak_count = 0;
        int online_count = 0;
        for(JSONObject json : typeinfo_transform){
            int brand_id = json.getInteger("brand_id");
            if(brand_ids.contains(brand_id)){
                continue;
            }

            bak_count = bak_count + 1;

            String brand = json.getString("brand");
            String first_char = Utils.getFirstLetter(brand);
            if(brand.startsWith("长")){
                first_char = "C";
            }

            brand_ids.add(brand_id);

            String logo = "unknown";
            if(online_brands.containsKey(brand_id)){
                online_count = online_count + 1;
                pstmt_online.setString(1, first_char);
                pstmt_online.setString(2,brand);
                pstmt_online.setInt(3, brand_id);
                pstmt_online.setString(4, logo);
                pstmt_online.setString(5, language);
                pstmt_online.setString(6, first_char);
                pstmt_online.setString(7, brand);
                pstmt_online.addBatch();
            }

            pstmt.setString(1, first_char);
            pstmt.setString(2,brand);
            pstmt.setInt(3, brand_id);
            pstmt.setString(4, logo);
            pstmt.setString(5, language);
            pstmt.setString(6, first_char);
            pstmt.setString(7, brand);
            pstmt.addBatch();

            if(bak_count % 1000 == 0){
                pstmt.executeBatch();
                logger.info("insertNewBrand to [carbarn_bak.car_brand_all] table " + bak_count + " datas");
            }

            if(online_count % 1000 == 0){
                pstmt_online.executeBatch();
                logger.info("insertNewBrand to [carbarn.car_brand] table " + online_count + " datas");
            }
        }

        pstmt.executeBatch();
        logger.info("insertNewBrand to [carbarn_bak.car_brand_all] table " + bak_count + " datas");

        pstmt_online.executeBatch();
        logger.info("insertNewBrand to [carbarn.car_brand] table " + online_count + " datas");
    }


    public static void insertNewSeries(Connection conn,
                                       List<JSONObject> typeinfo_transform,
                                       Map<Integer, String> online_brands,
                                       String language) throws SQLException {
        logger.info("---------start to insert new Series----------------------------");
        String sql_format = "INSERT INTO %s.%s (first_char, series, series_id, brand_id, `language`) \n" +
                "VALUES (?,?,?,?,?)\n" +
                "ON DUPLICATE KEY UPDATE\n" +
                "first_char = ?,\n" +
                "series = ?, \n" +
                "brand_id = ?, \n" +
                "update_time = NOW()";

        String insert_carbarn_bak_series_sql = String.format(sql_format, "carbarn_bak", "car_series_all");
        PreparedStatement bak_pstmt = conn.prepareStatement(insert_carbarn_bak_series_sql);


        String insert_carbarn_series_sql = String.format(sql_format, "carbarn", "car_series");
        PreparedStatement online_pstmt = conn.prepareStatement(insert_carbarn_series_sql);


        Set<Integer> series_ids = new HashSet<Integer>();
        int bak_count = 0;
        int online_count = 0;
        for(JSONObject json : typeinfo_transform){
            int series_id = json.getInteger("series_id");
            if(series_ids.contains(series_id)){
                continue;
            }

            series_ids.add(series_id);

            bak_count = bak_count + 1;
            String series = json.getString("series");
            int brand_id = json.getInteger("brand_id");
            String first_char = Utils.getFirstLetter(series);
            if(series.contains("进口")){
                first_char = "进口";
            }else if(series.startsWith("长")){
                first_char = "C";
            }

            if(online_brands.containsKey(brand_id)){
                online_count = online_count + 1;
                online_pstmt.setString(1, first_char);
                online_pstmt.setString(2,series);
                online_pstmt.setInt(3, series_id);
                online_pstmt.setInt(4, brand_id);
                online_pstmt.setString(5, language);
                online_pstmt.setString(6, first_char);
                online_pstmt.setString(7, series);
                online_pstmt.setInt(8, brand_id);
                online_pstmt.addBatch();
            }
            bak_pstmt.setString(1, first_char);
            bak_pstmt.setString(2,series);
            bak_pstmt.setInt(3, series_id);
            bak_pstmt.setInt(4, brand_id);
            bak_pstmt.setString(5, language);
            bak_pstmt.setString(6, first_char);
            bak_pstmt.setString(7, series);
            bak_pstmt.setInt(8, brand_id);
            bak_pstmt.addBatch();

            if(bak_count % 1000 == 0 && bak_count > 0){
                bak_pstmt.executeBatch();
                logger.info("insertNewSeries to [carbarn_bak.car_series_all] table " + bak_count + " datas");
            }

            if(online_count % 1000 == 0 && online_count > 0){
                online_pstmt.executeBatch();
                logger.info("insertNewSeries to [carbarn.car_series] table " + online_count + " datas");
            }
        }

        bak_pstmt.executeBatch();
        logger.info("insertNewSeries to [carbarn_bak.car_series_all] table " + bak_count + " datas");

        online_pstmt.executeBatch();
        logger.info("insertNewSeries to [carbarn.car_series_all] table " + online_count + " datas");
    }


    public static void insertNewType(Connection conn,
                                     List<JSONObject> typeinfo_transform,
                                     Map<Integer, String> online_brands,
                                     String language) throws SQLException {

        try{

            logger.info("---------start to insert new Types----------------------------");

            String sql_format = "INSERT INTO %s.%s (type,type_id,series_id,brand_id,year,`language`,price,floor_price," +
                    "guide_price,displacement,displacement_type,power,color," +
                    "transmission,emission_standards,type_of_manu,engine,drive_type,box,type_of_car,seat_capacity) \n" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)\n" +
                    "ON DUPLICATE KEY UPDATE\n" +
                    "type= ?,\n" +
                    "series_id= ?,\n" +
                    "brand_id= ?,\n" +
                    "year= ?,\n" +
                    "price= ?,\n" +
                    "floor_price= ?,\n" +
                    "guide_price= ?,\n" +
                    "displacement= ?,\n" +
                    "displacement_type= ?,\n" +
                    "power= ?,\n" +
                    "color= ?,\n" +
                    "transmission= ?,\n" +
                    "emission_standards= ?,\n" +
                    "type_of_manu= ?,\n" +
                    "engine= ?,\n" +
                    "drive_type= ?,\n" +
                    "box= ?,\n" +
                    "type_of_car= ?,\n" +
                    "seat_capacity =?, \n" +
                    "update_time = NOW()";

//        String insert_carbarn_bak_type_sql = String.format(sql_format, "carbarn_bak", "car_type_all");
//        PreparedStatement bak_pstmt = conn.prepareStatement(insert_carbarn_bak_type_sql);


            String insert_carbarn_type_sql = String.format(sql_format, "carbarn", "car_type");
            PreparedStatement online_pstmt = conn.prepareStatement(insert_carbarn_type_sql);
            int bak_count = 0;
            int online_count = 0;
            for(JSONObject json : typeinfo_transform){
                bak_count = bak_count + 1;
                String type = json.getString("type");
                int type_id = json.getInteger("type_id");
                int series_id = json.getInteger("series_id");
                int brand_id = json.getInteger("brand_id");
                String year = json.getString("year");
                double price = json.getDouble("price");
                double floor_price = json.getDouble("floor_price");
                double guide_price = json.getDouble("guide_price");
                double displacement = json.getDouble("displacement");
                String displacement_type = json.getString("displacement_type");
                double power = json.getDouble("power");
                int color = json.getInteger("color");
                int transmission = json.getInteger("transmission");
                int emission_standards = json.getInteger("emission_standards");
                int type_of_manu = json.getInteger("type_of_manu");
                int engine = json.getInteger("engine");
                int drive_type = json.getInteger("drive_type");
                int box = json.getInteger("box");
                int type_of_car = json.getInteger("type_of_car");
                int seat_capacity = json.getInteger("seat_capacity");

                if(online_brands.containsKey(brand_id)){
                    online_count = online_count + 1;
                    online_pstmt.setString(1,type);
                    online_pstmt.setInt(2,type_id);
                    online_pstmt.setInt(3,series_id);
                    online_pstmt.setInt(4,brand_id);
                    online_pstmt.setString(5,year);
                    online_pstmt.setString(6,language);
                    online_pstmt.setDouble(7,price);
                    online_pstmt.setDouble(8,floor_price);
                    online_pstmt.setDouble(9,guide_price);
                    online_pstmt.setDouble(10,displacement);
                    online_pstmt.setString(11,displacement_type);
                    online_pstmt.setDouble(12,power);
                    online_pstmt.setInt(13,color);
                    online_pstmt.setInt(14,transmission);
                    online_pstmt.setInt(15,emission_standards);
                    online_pstmt.setInt(16,type_of_manu);
                    online_pstmt.setInt(17,engine);
                    online_pstmt.setInt(18,drive_type);
                    online_pstmt.setInt(19,box);
                    online_pstmt.setInt(20,type_of_car);
                    online_pstmt.setInt(21,seat_capacity);
                    online_pstmt.setString(22,type);
                    online_pstmt.setInt(23,series_id);
                    online_pstmt.setInt(24,brand_id);
                    online_pstmt.setString(25,year);
                    online_pstmt.setDouble(26,price);
                    online_pstmt.setDouble(27,floor_price);
                    online_pstmt.setDouble(28,guide_price);
                    online_pstmt.setDouble(29,displacement);
                    online_pstmt.setString(30,displacement_type);
                    online_pstmt.setDouble(31,power);
                    online_pstmt.setInt(32,color);
                    online_pstmt.setInt(33,transmission);
                    online_pstmt.setInt(34,emission_standards);
                    online_pstmt.setInt(35,type_of_manu);
                    online_pstmt.setInt(36,engine);
                    online_pstmt.setInt(37,drive_type);
                    online_pstmt.setInt(38,box);
                    online_pstmt.setInt(39,type_of_car);
                    online_pstmt.setInt(40,seat_capacity);
                    online_pstmt.addBatch();
                }

//            bak_pstmt.setString(1,type);
//            bak_pstmt.setInt(2,type_id);
//            bak_pstmt.setInt(3,series_id);
//            bak_pstmt.setInt(4,brand_id);
//            bak_pstmt.setString(5,year);
//            bak_pstmt.setString(6,language);
//            bak_pstmt.setDouble(7,price);
//            bak_pstmt.setDouble(8,floor_price);
//            bak_pstmt.setDouble(9,guide_price);
//            bak_pstmt.setDouble(10,displacement);
//            bak_pstmt.setString(11,displacement_type);
//            bak_pstmt.setDouble(12,power);
//            bak_pstmt.setInt(13,color);
//            bak_pstmt.setInt(14,transmission);
//            bak_pstmt.setInt(15,emission_standards);
//            bak_pstmt.setInt(16,type_of_manu);
//            bak_pstmt.setInt(17,engine);
//            bak_pstmt.setInt(18,drive_type);
//            bak_pstmt.setInt(19,box);
//            bak_pstmt.setInt(20,type_of_car);
//            bak_pstmt.setInt(21,seat_capacity);
//            bak_pstmt.setString(22,type);
//            bak_pstmt.setInt(23,series_id);
//            bak_pstmt.setInt(24,brand_id);
//            bak_pstmt.setString(25,year);
//            bak_pstmt.setDouble(26,price);
//            bak_pstmt.setDouble(27,floor_price);
//            bak_pstmt.setDouble(28,guide_price);
//            bak_pstmt.setDouble(29,displacement);
//            bak_pstmt.setString(30,displacement_type);
//            bak_pstmt.setDouble(31,power);
//            bak_pstmt.setInt(32,color);
//            bak_pstmt.setInt(33,transmission);
//            bak_pstmt.setInt(34,emission_standards);
//            bak_pstmt.setInt(35,type_of_manu);
//            bak_pstmt.setInt(36,engine);
//            bak_pstmt.setInt(37,drive_type);
//            bak_pstmt.setInt(38,box);
//            bak_pstmt.setInt(39,type_of_car);
//            bak_pstmt.setInt(40,seat_capacity);
//            bak_pstmt.addBatch();

//            if(bak_count % 1000 == 0 && bak_count > 0){
//                bak_pstmt.executeBatch();
//                logger.info("insertNewType to [carbarn_bak.car_type_all] table " + bak_count + " datas");
//            }

                if(online_count % 1000 == 0 && online_count > 0){
                    online_pstmt.executeBatch();
                    logger.info("insertNewType to [carbarn.car_type] table " + online_count + " datas");
                }
            }

//        bak_pstmt.executeBatch();
//        logger.info("insertNewType to [carbarn_bak.car_type_all] table " + bak_count + " datas");

            online_pstmt.executeBatch();
            online_pstmt.close();
            logger.info("insertNewType to [carbarn.car_type] table " + online_count + " datas");

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void updateTypeDetails(Connection conn,
                                         List<JSONObject> typedetails_transform,
                                         Map<Integer, String> online_brands,
                                         String language) throws SQLException {
        logger.info("---------start to update Type Details----------------------------");
        String sql_format = "update %s.%s \n" +
                "set battery_capacity = ?, \n" +
                "pure_electric_range = ?, \n" +
                "details = ? \n" +
                "where type_id = ? and `language` = ?";

//        String insert_carbarn_bak_type_sql = String.format(sql_format, "carbarn_bak", "car_type_all");
//        PreparedStatement bak_pstmt = conn.prepareStatement(insert_carbarn_bak_type_sql);

        try{
            String insert_carbarn_type_sql = String.format(sql_format, "carbarn", "car_type");

            PreparedStatement online_pstmt = conn.prepareStatement(insert_carbarn_type_sql);

            int bak_count = 0;
            int online_count = 0;
            for(JSONObject json : typedetails_transform){
                bak_count = bak_count + 1;
                online_count = online_count + 1;
                int type_id = json.getInteger("type_id");
                String details = json.getString("params");

                JSONObject battery_infos = Transform.getBatteryInfosFromDetails(details);
                double battery_capacity = battery_infos.getDouble("battery_capacity");
                double pure_electric_range = battery_infos.getDouble("pure_electric_range");

//            bak_pstmt.setDouble(1, battery_capacity);
//            bak_pstmt.setDouble(2, pure_electric_range);
//            bak_pstmt.setString(3, details);
//            bak_pstmt.setInt(4, type_id);
//            bak_pstmt.setString(5, language);
//            bak_pstmt.addBatch();

                online_pstmt.setDouble(1, battery_capacity);
                online_pstmt.setDouble(2, pure_electric_range);
                online_pstmt.setString(3, details);
                online_pstmt.setInt(4, type_id);
                online_pstmt.setString(5, language);
                online_pstmt.addBatch();

//            if(bak_count % 1000 == 0){
//                bak_pstmt.executeBatch();
//                logger.info("updateTypeDetails to [carbarn_bak.car_type_all] table " + bak_count + " datas");
//            }

                if(online_count % 1000 == 0){
                    online_pstmt.executeBatch();
                    logger.info("updateTypeDetails to [carbarn.car_type_all] table " + online_count + " datas");
                }
            }

//        bak_pstmt.executeBatch();
//        logger.info("updateTypeDetails to [carbarn_bak.car_type_all] table " + bak_count + " datas");

            online_pstmt.executeBatch();
            logger.info("updateTypeDetails to [carbarn.car_type] table " + online_count + " datas");
            online_pstmt.close();

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public static void insertNewSummary(Connection conn, String version, String release_date, String info) {
        String sql_format = "insert into qixiubao_update_version(`version`, release_date, info)" +
                "value('%s', '%s', '%s')";

        try{
            String insert_sql = String.format(sql_format, version, release_date, info);
            System.out.println(insert_sql);
            Statement statement = conn.createStatement();
            statement.execute(insert_sql);
            statement.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Map<String, String> getLastUpdateVersion(Connection conn) {
        Map<String, String>  result = new HashMap<String,String>();
        try{
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from carbarn.qixiubao_update_version order by update_time desc limit 1");
            while(resultSet.next()){
                String version = resultSet.getString("version");
                String release_date = resultSet.getString("release_date");
                result.put("version", version);
                result.put("release_date", release_date);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
