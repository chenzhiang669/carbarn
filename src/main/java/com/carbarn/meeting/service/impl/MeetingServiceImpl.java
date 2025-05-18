package com.carbarn.meeting.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.CarsService;
import com.carbarn.meeting.feishu.FeishuApplyReserve;
import com.carbarn.meeting.mapper.MeetingMapper;
import com.carbarn.meeting.pojo.MeetingPOJO;
import com.carbarn.meeting.pojo.dto.MeetingCarInfoDTO;
import com.carbarn.meeting.pojo.dto.MeetingFullMessageDTO;
import com.carbarn.meeting.pojo.dto.MeetingUserInfoDTO;
import com.carbarn.meeting.pojo.dto.SearchMeetingDTO;
import com.carbarn.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class MeetingServiceImpl implements MeetingService {
    public static DateTimeFormatter dateTimeFormater1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    @Autowired
    private MeetingMapper meetingMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarsService carsService;
    @Autowired
    private ParamsMapper paramsMapper;

    @Override
    public MeetingFullMessageDTO createNewMeeting(String language, MeetingPOJO meetingPOJO) {
        int car_id = meetingPOJO.getCar_id();
        long buyer_id = meetingPOJO.getBuyer_id();
        long seller_id = meetingPOJO.getSeller_id();
        String datetime = LocalDateTime.now().format(dateTimeFormater1);
        String meeting_id = datetime + "-" + car_id + "-" + buyer_id + "-" + seller_id; //生成会议id: 时间(年月日时分秒)-汽车id-买家id-卖家id
        meetingPOJO.setMeeting_id(meeting_id);
        meetingMapper.createNewMeeting(meetingPOJO);

        return getMeetingInfo(language, meeting_id);
    }

    @Override
    public void setMeetingTime(String language, MeetingPOJO meetingPOJO) {
        meetingMapper.setMeetingTime(meetingPOJO);
    }

    @Override
    public List<MeetingPOJO> getUserMeetings(long user_id, SearchMeetingDTO searchMeetingDTO) {

        long current_time = System.currentTimeMillis();
        return meetingMapper.getUserMeetings(user_id, current_time, searchMeetingDTO);
    }

    @Override
    public void acceptMeetingInvite(String meeting_id) {
        MeetingPOJO meetingPOJO = meetingMapper.getMeetingInfo(meeting_id);
        String end_time = String.valueOf(meetingPOJO.getEnd_time() / 1000);
        String param_meeting = paramsMapper.getValue(ParamKeys.param_meeting);  //从参数库表中获取支付相关信息
        JSONObject json = JSON.parseObject(param_meeting);
        String appId = json.getString("appId");
        String appSecret = json.getString("appSecret");
        String userIdType = json.getString("userIdType");
        String ownerId = json.getString("ownerId");
        String topic = json.getString("topic");

        JSONObject feishumeeting = FeishuApplyReserve.createFeishuMeeting(appId, appSecret, userIdType, ownerId, topic, end_time);
        MeetingPOJO meetingPOJO1 = feishumeeting.toJavaObject(MeetingPOJO.class);
        String app_link = meetingPOJO1.getApp_link().replace("preview={?}", "preview=1");
        meetingPOJO1.setApp_link(app_link);
        meetingPOJO1.setMeeting_id(meeting_id);
        meetingMapper.updateMeeting(meetingPOJO1);
    }


    @Override
    public MeetingFullMessageDTO getMeetingInfo(String language, String meeting_id) {
        MeetingPOJO meetingPOJO = meetingMapper.getMeetingInfo(meeting_id);
        long buyer_id = meetingPOJO.getBuyer_id();
        long seller_id = meetingPOJO.getSeller_id();
        int car_id = meetingPOJO.getCar_id();
        UserPojo buyerInfo = userMapper.getUserInfoByID(buyer_id);
        UserPojo sellerInfo = userMapper.getUserInfoByID(seller_id);
        Map<String, Object> carsInfo = carsService.getCarsByID(language, car_id);

        return transformContractInfo(language, meetingPOJO, buyerInfo, sellerInfo, carsInfo);
    }

    private MeetingFullMessageDTO transformContractInfo(String language,
                                                         MeetingPOJO meetingPOJO,
                                                         UserPojo buyerInfo,
                                                         UserPojo sellerInfo,
                                                         Map<String, Object> carsInfo) {
        MeetingUserInfoDTO meetingBuyerInfoDTO = MeetingUserInfoDTO.getMeetingBuyerInfoDTO(buyerInfo);
        MeetingUserInfoDTO meetingSellerInfoDTO = MeetingUserInfoDTO.getMeetingBuyerInfoDTO(sellerInfo);
        MeetingCarInfoDTO meetingCarInfoDTO = MeetingCarInfoDTO.getMeetingCarInfoDTO(carsInfo);

        MeetingFullMessageDTO meetingInfo = MeetingFullMessageDTO.getMeetingFullMessageDTO(meetingPOJO);
        meetingInfo.setBuyer_info(meetingBuyerInfoDTO);
        meetingInfo.setSeller_info(meetingSellerInfoDTO);
        meetingInfo.setCar_info(meetingCarInfoDTO);

        return meetingInfo;
    }
}