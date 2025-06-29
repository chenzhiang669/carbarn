package com.carbarn.meeting.feishu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.utils.qixiubao.update.LocalRunForbidenLog4j;
import com.carbarn.meeting.mapper.MeetingMapper;
import com.carbarn.meeting.pojo.dto.FinishedMeetingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DeleteFromCompany {

    private static final Logger logger = LoggerFactory.getLogger(DeleteFromCompany.class);

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private ParamsMapper paramsMapper;

    private static String getOpenId(String invite_infos) {
        try {
            JSONObject tmp = JSON.parseObject(invite_infos);
            if (tmp.containsKey("user")) {
                JSONObject user = tmp.getJSONObject("user");
                if (user.containsKey("open_id")) {
                    String open_id = user.getString("open_id");
                    return open_id;
                }
            }
        } catch (Exception e) {
            logger.warn("parse open_id from " + invite_infos);
        }

        return null;
    }

    @Scheduled(fixedDelay = 60000)
    private void deleteFromCompany() throws Exception {


        String value = paramsMapper.getValue(ParamKeys.param_meeting);
        logger.info("value:" + value);
        JSONObject meeting_params = JSON.parseObject(value);
        String appId = meeting_params.getString("appId");
        String appSecret = meeting_params.getString("appSecret");
        String lark_appId = meeting_params.getString("lark_appId");
        String lark_appSecret = meeting_params.getString("lark_appSecret");
        long delete_minute_interval = meeting_params.getLong("delete_minute_interval");

        //1、从meeting表中获取当前需要邀请的用户加入公司的用户
        long currentTime = System.currentTimeMillis();
        logger.info("currentTime: " + currentTime);
        long time_interval = delete_minute_interval * 60 * 1000;
        logger.info("~~~~~~~~~~~~~~~~start to get Meetings which has been finished over " + delete_minute_interval + " minutes ~~~~~~~~~~~~~~~~");
        List<FinishedMeetingDTO> delete_meetings = meetingMapper.isFinishedMeetings(currentTime, time_interval);

        if (delete_meetings.size() != 0) {
            logger.info("~~~~~~~~~~~~~~~~there are " + delete_meetings.size() + " meetings over " + delete_minute_interval + " minutes ~~~~~~~~~~~~~~~~");
            for (FinishedMeetingDTO finishedMeetingDTO : delete_meetings) {

                String buyer_invite_infos = finishedMeetingDTO.getBuyer_invite_infos();
                String seller_invite_infos = finishedMeetingDTO.getSeller_invite_infos();
                String meeting_id = finishedMeetingDTO.getMeeting_id();

                if (buyer_invite_infos != null) {
                    String open_id = getOpenId(buyer_invite_infos);
                    logger.info(open_id);
                    if (open_id != null) {
                        FeishuApplyReserve.deleteUserFromCompany(lark_appId, lark_appSecret, open_id, "open_id");
                        logger.info("detele meeting_id:{} --> open_id:{} from company successfully", meeting_id, open_id);
                    }
                }

                if (seller_invite_infos != null) {
                    String open_id = getOpenId(seller_invite_infos);
                    logger.info(open_id);
                    if (open_id != null) {
                        FeishuApplyReserve.deleteUserFromCompany(appId, appSecret, open_id, "open_id");
                        logger.info("detele meeting_id:{} --> open_id:{} from company successfully", meeting_id, open_id);
                    }
                }

                meetingMapper.update_is_invite_delete(meeting_id);
                logger.info("update is_invite_delete = 1 where  meeting_id = {}", meeting_id);
            }

            logger.info("these are " + delete_meetings.size() + "meetings has been deleted!");
        }else{
            logger.info("there is no meetings finished!");
        }


    }


}
