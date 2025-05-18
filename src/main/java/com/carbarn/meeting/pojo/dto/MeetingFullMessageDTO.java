package com.carbarn.meeting.pojo.dto;

import com.carbarn.meeting.pojo.MeetingPOJO;
import lombok.Data;

@Data
public class MeetingFullMessageDTO {
    private String meeting_id;
    private long buyer_id;
    private int buyer_timezone;
    private long seller_id;
    private int seller_timezone;
    private int car_id;
    private long start_time;
    private long end_time;
    private String description;
    private String app_link;
    private String live_link;
    private String meeting_no;
    private String url;

    private MeetingUserInfoDTO buyer_info;
    private MeetingUserInfoDTO seller_info;
    private MeetingCarInfoDTO car_info;


    public static MeetingFullMessageDTO getMeetingFullMessageDTO(MeetingPOJO meetingPOJO) {
        MeetingFullMessageDTO meetingFullMessageDTO = new MeetingFullMessageDTO();
        meetingFullMessageDTO.setMeeting_id(meetingPOJO.getMeeting_id());
        meetingFullMessageDTO.setBuyer_id(meetingPOJO.getBuyer_id());
        meetingFullMessageDTO.setBuyer_timezone(meetingPOJO.getBuyer_timezone());
        meetingFullMessageDTO.setSeller_id(meetingPOJO.getSeller_id());
        meetingFullMessageDTO.setSeller_timezone(meetingPOJO.getSeller_timezone());
        meetingFullMessageDTO.setCar_id(meetingPOJO.getCar_id());
        meetingFullMessageDTO.setStart_time(meetingPOJO.getStart_time());
        meetingFullMessageDTO.setEnd_time(meetingPOJO.getEnd_time());
        meetingFullMessageDTO.setDescription(meetingPOJO.getDescription());
        meetingFullMessageDTO.setApp_link(meetingPOJO.getApp_link());
        meetingFullMessageDTO.setLive_link(meetingPOJO.getLive_link());
        meetingFullMessageDTO.setMeeting_no(meetingPOJO.getMeeting_no());
        meetingFullMessageDTO.setUrl(meetingPOJO.getUrl());
        return meetingFullMessageDTO;
    }
}
