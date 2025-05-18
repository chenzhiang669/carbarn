package com.carbarn.meeting.service;


import com.carbarn.contract.pojo.dto.ContractFullMessageDTO;
import com.carbarn.meeting.pojo.MeetingPOJO;
import com.carbarn.meeting.pojo.dto.MeetingFullMessageDTO;
import com.carbarn.meeting.pojo.dto.SearchMeetingDTO;

import java.util.List;

public interface MeetingService {

    MeetingFullMessageDTO createNewMeeting(String language, MeetingPOJO meetingPOJO);

    void acceptMeetingInvite(String meetingPOJO);

    MeetingFullMessageDTO getMeetingInfo(String language, String meeting_id);

    void setMeetingTime(String language, MeetingPOJO meetingPOJO);

    List<MeetingPOJO> getUserMeetings(long user_id, SearchMeetingDTO searchMeetingDTO);
}
