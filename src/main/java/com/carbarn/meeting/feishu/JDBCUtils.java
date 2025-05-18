package com.carbarn.meeting.feishu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static JSONObject getMeetingParams(Connection conn){
        try{
            Statement statement = conn.createStatement();
            String sql = "select * from carbarn.params where `key` = 'com.carbarn.param.meeting' limit 1";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String value = resultSet.getString("value");
                JSONObject json = JSON.parseObject(value);
                return json;
            }

        }catch (Exception e){

        }

        return null;
    }

    //获取即将开始的会议，以及会议双方的昵称和电话号码
    public static List<JSONObject> isGoingToStartMeeting(Connection conn,
                                                   long currentTime,
                                                   long time_interval){

        List<JSONObject> result = new ArrayList<JSONObject>();
        try{
            Statement statement = conn.createStatement();

            //从meeting表中获取10分钟内即将开始的会议，也即start_time - currentTime < time_interval（10分钟）
            String sql_format = "select \n" +
                    "a.*,\n" +
                    "b.nickname as buyer_nickname,\n" +
                    "b.phone_num as buyer_phonenum,\n" +
                    "c.nickname as seller_nickname,\n" +
                    "c.phone_num as seller_phonenum\n" +
                    "from \n" +
                    "(select \n" +
                    "*\n" +
                    "from \n" +
                    "carbarn.meeting\n" +
                    "where \n" +
                    "start_time > %s\n" +
                    "and start_time - %s < %s\n" +
                    "and meeting_no is not null\n" +
                    "and buyer_invite_infos is null \n" +
                    "and seller_invite_infos is null \n" +
                    ") a \n" +
                    "left join (select * from users) b on a.buyer_id = b.id \n" +
                    "left join (select * from users) c on a.seller_id = c.id";
            String sql = String.format(sql_format, currentTime, currentTime, time_interval);
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String meeting_id = resultSet.getString("meeting_id");
                String buyer_nickname = resultSet.getString("buyer_nickname");
                String buyer_phonenum = resultSet.getString("buyer_phonenum");
                String seller_nickname = resultSet.getString("seller_nickname");
                String seller_phonenum = resultSet.getString("seller_phonenum");

                JSONObject json = new JSONObject();
                json.put("meeting_id",meeting_id);
                json.put("buyer_nickname",buyer_nickname);
                json.put("buyer_phonenum",buyer_phonenum);
                json.put("seller_nickname",seller_nickname);
                json.put("seller_phonenum",seller_phonenum);

                result.add(json);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;

    }

    public static List<JSONObject> isFinishedMeetings(Connection conn,
                                                         long currentTime,
                                                         long time_interval){

        List<JSONObject> result = new ArrayList<JSONObject>();
        try{
            Statement statement = conn.createStatement();

            //从meeting表中获取已经结束超过1分钟的会议，也即currentTime - endTime > time_interval（10分钟）
            String sql_format = "select \n" +
                    "*\n" +
                    "from \n" +
                    "meeting\n" +
                    "where \n" +
                    "%s - end_time >= %s\n" +
                    "and meeting_no is not null\n" +
                    "and is_invite_delete = 0 \n" +
                    "and (buyer_invite_infos is not null or seller_invite_infos is not null)";
            String sql = String.format(sql_format, currentTime, time_interval);
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String meeting_id = resultSet.getString("meeting_id");
                String buyer_invite_infos = resultSet.getString("buyer_invite_infos");
                String seller_invite_infos = resultSet.getString("seller_invite_infos");

                JSONObject json = new JSONObject();
                json.put("meeting_id",meeting_id);
                json.put("buyer_invite_infos",buyer_invite_infos);
                json.put("seller_invite_infos",seller_invite_infos);

                result.add(json);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;

    }

    public static void updateInviteInfos(Connection conn,
                                         List<JSONObject> invite_to_company_infos) throws SQLException {

        String update_sql = "update meeting \n" +
                "set buyer_invite_infos = ?,\n" +
                "seller_invite_infos = ?\n" +
                "where meeting_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(update_sql);

        int count = 0;
        for(JSONObject json : invite_to_company_infos){

            count = count + 1;
            pstmt.setString(1, json.getString("buyer_invite_infos"));
            pstmt.setString(2,json.getString("seller_invite_infos"));
            pstmt.setString(3, json.getString("meeting_id"));
            pstmt.addBatch();

            if(count % 1000 == 0){
                pstmt.executeBatch();
                logger.info("updateInviteInfos [carbarn.meeting] table " + count + " datas");
            }
        }

        pstmt.executeBatch();
        logger.info("updateInviteInfos [carbarn.meeting] table " + count + " datas");

    }


    public static void update_is_invite_delete(Connection conn,
                                                List<JSONObject> invite_to_company_infos) throws SQLException {

        String update_sql = "update meeting \n" +
                "set is_invite_delete = 1 \n" +
                "where meeting_id = ?";

        PreparedStatement pstmt = conn.prepareStatement(update_sql);

        int count = 0;
        for(JSONObject json : invite_to_company_infos){

            count = count + 1;
            pstmt.setString(1, json.getString("meeting_id"));
            pstmt.addBatch();

            if(count % 1000 == 0){
                pstmt.executeBatch();
                logger.info("update is_invite_delete [carbarn.meeting] table " + count + " datas");
            }
        }

        pstmt.executeBatch();
        logger.info("update is_invite_delete [carbarn.meeting] table " + count + " datas");
    }
}
