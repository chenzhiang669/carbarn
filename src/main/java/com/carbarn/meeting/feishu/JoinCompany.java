package com.carbarn.meeting.feishu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.utils.qixiubao.update.LocalRunForbidenLog4j;
import com.carbarn.meeting.mapper.MeetingMapper;
import com.carbarn.meeting.pojo.dto.GoingToStartMeetingDTO;
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
public class JoinCompany {

    private static final Logger logger = LoggerFactory.getLogger(JoinCompany.class);

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private ParamsMapper paramsMapper;

    @Scheduled(fixedDelay = 30000)
    private void joinCompany() throws Exception {


        String value = paramsMapper.getValue(ParamKeys.param_meeting);
        logger.info("value:" + value);
        JSONObject meeting_params = JSON.parseObject(value);
        String appId = meeting_params.getString("appId");
        String appSecret = meeting_params.getString("appSecret");
        String userIdType = meeting_params.getString("userIdType");
        String ownerId = meeting_params.getString("ownerId");
        long minute_interval = meeting_params.getLong("minute_interval");

        logger.info("meeting_params appId:{} | appSecret:{} | userIdType:{} | ownerId:{} | minute_interval:{}", appId, appSecret, userIdType, ownerId, minute_interval);
        //1、从meeting表中获取当前需要邀请的用户加入公司的用户
        long currentTime = System.currentTimeMillis();
        logger.info("currentTime: " + currentTime);
        long time_interval = minute_interval * 60 * 1000;
        logger.info("start to get Meetings which are starting in " + minute_interval + " minutes");
        List<GoingToStartMeetingDTO> invite_meetings = meetingMapper.isGoingToStartMeeting(currentTime, time_interval);

        if (invite_meetings.size() != 0) {
            logger.info("~~~~~~~~~~~~~~~~there are " + invite_meetings.size() + " meetings in " + minute_interval + " minutes ~~~~~~~~~~~~~~~~");
            Set<String> phonenums = new HashSet<String>();
            for (GoingToStartMeetingDTO goingToStartMeetingDTO : invite_meetings) {
                logger.info("start process meeting_id: " + goingToStartMeetingDTO.getMeeting_id());
                String buyer_nickname = goingToStartMeetingDTO.getBuyer_nickname();
                String buyer_phonenum = goingToStartMeetingDTO.getBuyer_phonenum();
                String seller_nickname = goingToStartMeetingDTO.getSeller_nickname();
                String seller_phonenum = goingToStartMeetingDTO.getSeller_phonenum();
                String meeting_id = goingToStartMeetingDTO.getMeeting_id();

                String buyer_invite_infos = null;
                String seller_invite_infos = null;
                if (!phonenums.contains(buyer_phonenum)) {
                    buyer_invite_infos = FeishuApplyReserve.inviteJoinCompany(appId, appSecret, buyer_phonenum, buyer_nickname, ownerId, userIdType, "open_department_id", new String[]{"0"});
                }

                if (!phonenums.contains(seller_phonenum)) {
                    seller_invite_infos = FeishuApplyReserve.inviteJoinCompany(appId, appSecret, seller_phonenum, seller_nickname, ownerId, userIdType, "open_department_id", new String[]{"0"});
                }

                if(buyer_invite_infos != null || seller_invite_infos != null){
                    logger.info("update meeting_id: " + meeting_id + " [buyer_invite_infos, seller_invite_infos]");
                    meetingMapper.updateInviteInfos(meeting_id, buyer_invite_infos, seller_invite_infos);
                }

            }

            logger.info("these " + invite_meetings.size() + "meetings has been processed.");

        }else{
            logger.info("there is no meetings to be processed");
        }


    }
}
