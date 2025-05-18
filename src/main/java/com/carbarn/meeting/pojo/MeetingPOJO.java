package com.carbarn.meeting.pojo;

import lombok.Data;

@Data
public class MeetingPOJO {
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
}
