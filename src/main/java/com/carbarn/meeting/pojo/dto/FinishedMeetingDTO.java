package com.carbarn.meeting.pojo.dto;

import lombok.Data;

@Data
public class FinishedMeetingDTO {
    private String meeting_id;
    private String buyer_invite_infos;
    private String seller_invite_infos;
}
