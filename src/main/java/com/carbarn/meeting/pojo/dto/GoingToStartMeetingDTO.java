package com.carbarn.meeting.pojo.dto;

import lombok.Data;

@Data
public class GoingToStartMeetingDTO {
    private String meeting_id;
    private String buyer_nickname;
    private String buyer_phonenum;
    private String seller_nickname;
    private String seller_phonenum;
    private String buyer_email;
}
