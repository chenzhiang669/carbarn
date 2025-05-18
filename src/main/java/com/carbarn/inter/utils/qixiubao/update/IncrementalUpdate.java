package com.carbarn.inter.utils.qixiubao.update;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.test.Translator1;
import com.carbarn.inter.utils.qixiubao.QixiubaoHttp;
import com.carbarn.inter.utils.qixiubao.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class IncrementalUpdate {

    private static final Logger logger = LoggerFactory.getLogger(IncrementalUpdate.class);

    public static List<JSONObject> getTypeDetails(List<String> typeinfos){
        List<JSONObject> typeDetails = new ArrayList<JSONObject>();

        //获取车型详细参数（转化为车出海格式之后的数据）
        for (int page = 0; page < typeinfos.size(); page++) {
            String typeinfo = typeinfos.get(page);
            try {
                JSONObject typeinfo_json = JSON.parseObject(typeinfo);
                if (!typeinfo_json.containsKey("result")) {
                    continue;
                }
                JSONObject result = typeinfo_json.getJSONObject("result");

                if (!result.containsKey("list")) {
                    continue;
                }
                JSONArray list = result.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    JSONObject data = list.getJSONObject(i);
                    if (!data.containsKey("id")) {
                        continue;
                    }
                    String type_id = data.getString("id");
                    JSONObject detail = QixiubaoHttp.searchTypeDetails(type_id);
                    typeDetails.add(detail);

                }

                logger.info("spider type details page " + page + " successfully");
            } catch (Exception e) {
                continue;
            }

        }

        return typeDetails;
    }



    private static boolean is_update(Connection connection) {
        Map<String, String> last_info = JDBCUtils.getLastUpdateVersion(connection);
        String last_version = last_info.get("version");
        String last_release_date = last_info.get("release_date");
        logger.info("last_version:{}, last_release_date:{}", last_version, last_release_date);

        String summary = QixiubaoHttp.searchSummary();
        logger.info(summary);
        JSONObject json = JSON.parseObject(summary);
        if(json.containsKey("result")){
            JSONObject result = json.getJSONObject("result");
            if(result.containsKey("version") && result.containsKey("release_date")){
                String version = result.getString("version");
                String release_date = result.getString("release_date");
                logger.info("version:{}, release_date:{}", version, release_date);
                if(!version.equals(last_version) || !release_date.equals(last_release_date)){
                    JDBCUtils.insertNewSummary(connection, version, release_date, summary);
                    return true;
                }
            }
        }
        return false;
    }

    public static void insertOtherLanguage(String ip,
                                           int port,
                                           String user,
                                           String password,
                                           String database,
                                           List<JSONObject> typeinfo_transform,
                                           String sourceLang,
                                           String targetLang,
                                           Map<Integer, String> online_brands) throws SQLException {
        Translator1 translate = new Translator1();
        List<JSONObject> translate_infos = new ArrayList<JSONObject>();
        for(JSONObject info:typeinfo_transform){
            JSONObject tmp = new JSONObject();
            tmp.putAll(info);
            String brand = tmp.getString("brand");
            String series = tmp.getString("series");
            String type = tmp.getString("type");

            String brand_trans = translate.translate(brand,sourceLang,targetLang);
            String series_trans = translate.translate(series,sourceLang,targetLang);
            String type_trans = translate.translate(type,sourceLang,targetLang);


            logger.info(brand + "\t" + series +"\t" + type) ;
            logger.info(brand_trans + "\t" + series_trans +"\t" + type_trans) ;
            if(!"".equals(brand_trans) && !"".equals(series_trans) && !"".equals(type_trans)){
                tmp.put("brand", brand_trans);
                tmp.put("series", series_trans);
                tmp.put("type", type_trans);
                translate_infos.add(tmp);
            }

            logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }

        Connection connection = JDBCUtils.getConnection(ip, port, user, password,database);
        JDBCUtils.insertNewBrand(connection, translate_infos, online_brands, targetLang);
        JDBCUtils.insertNewSeries(connection, translate_infos, online_brands, targetLang);
        JDBCUtils.insertNewType(connection, translate_infos, online_brands, targetLang);
        connection.close();

        logger.info("############################################################");
        logger.info("###### update language {} data successfully   ############", targetLang);
        logger.info("############################################################");
    }



    public static void main(String[] args) throws SQLException {
        //第一步、 增量数据查询以及转化
        //1、查询出汽修宝最近增量更新的车型数据(具体再看)
        //2、从第1步余到的新增车型信息里面解析出品牌、车系、车型、以及车型的相关信息
        //3、将汽修宝的车型信息转化为车出海的车型信息:typeInfoTransform()
        //4、根据车型id查询出车型的详细信息并转化为车出海的详细信息:typeDetailsTransform()
        //
        //至此，我们拿到了全部的新增数据信息，包括品牌、品牌id、车系、车系id、车型、车型1d、车型信息以及车型详维下面，我们需要将这些数据插入到我们的数据库中.
        //第二步、数据(中文版)更新到车出海数据表中
        //(这个地方有一个问题:我们的数据库中的品牌进行了精简。一些新的品牌我们是不需要的，但是需要做个保留。
        // 所以这里需要有个判断，在线数据表中是否包含搜索出来的品牌:selectOnlineBrands()
        //1、如果新增数据的品牌包含在在线品牌表中，则更新到在线数据表中:
        //2、如果新增数据的品牌不在在线品牌表中，则更新到备份表中;
        //
        //三、其他语言版本数据并插入到车出海数据表中
        //1、信息翻泽:translate(language)
        //2、如"第二步"中的逻辑，将数据插入到对应的数据表中
        //
        //
        //执行步骤:
        //1、调通增量数据查询接口(最重要的!!!!!!!!!!)
        //2、配置信息(mysql):
        //(1) 汽修宝和车出海车型信息的字段值映射关系表构建
        //(2) 翻译的语言表构建。目前已有可以复用language表
        //
        // 3、数据转化和更新
        //(1)汽修宝和车出海车型信息映射代码(冷启动里面有。拷贝一份)
        //(2)汽修宝和车出海车型详细信息映射代码(冷启动里面有，拷贝一份)
        //(3)插入更新逻辑代码(开发)

        String ip = args[0];
        int port = Integer.valueOf(args[1]);
        String user = args[2];
        String password = args[3];
        String database = args[4];

        LocalRunForbidenLog4j.forbidenlog();
        Connection connection = JDBCUtils.getConnection(ip, port, user, password,database);
        boolean is_update = is_update(connection);
        connection.close();
        if(!is_update){
            return;
        }



        List<String> typeinfos = QixiubaoHttp.searcIncrementTypes(); //获取增量车型数据；
//        List<String> typeinfos = QixiubaoHttp.searcTypes("75","5964"); //更新某个车系的数据信息；
        List<JSONObject> typeDetails = getTypeDetails(typeinfos); //获取车型的详细信息;

        connection = JDBCUtils.getConnection(ip, port, user, password,database);
        Map<String, Integer>  carbarn_id = JDBCUtils.getFieldIdMap(connection); //获取车出海字段值id
        Map<String, String> qixiubao_carbarn = JDBCUtils.getQixiubao_Carbarn_Map(connection); //获取汽修宝字段值和车出海字段值的映射关系
        List<JSONObject> typeinfo_transform = Transform.typeInfoTransform(typeinfos, qixiubao_carbarn, carbarn_id); //车型信息转化为车出海格式数据

        logger.info("############################################################");
        logger.info("###### all datas are downloaded and transformed ############");
        logger.info("############################################################");
        logger.info("------now we are starting update language 'zh' data --------------");

        Map<Integer, String> online_brands = JDBCUtils.getOnlineBrands(connection); //brand在线表中的品牌id及品牌
        JDBCUtils.insertNewBrand(connection, typeinfo_transform, online_brands, "zh");
        JDBCUtils.insertNewSeries(connection, typeinfo_transform, online_brands, "zh");
        JDBCUtils.insertNewType(connection, typeinfo_transform, online_brands, "zh");
        JDBCUtils.updateTypeDetails(connection, typeDetails, online_brands, "zh");
        connection.close();

        logger.info("############################################################");
        logger.info("###### update language 'zh' data successfully   ############");
        logger.info("############################################################");
        logger.info("------now we are starting update other languages--------------");

        List<String> languages = new ArrayList<String>();
        languages.add("en");
        languages.add("ru");

        for(String language:languages){
            insertOtherLanguage(ip, port, user, password,database, typeinfo_transform, "zh", language, online_brands);
        }
    }

}
