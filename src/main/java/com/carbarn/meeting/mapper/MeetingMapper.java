package com.carbarn.meeting.mapper;

import com.alibaba.fastjson.JSONObject;
import com.carbarn.meeting.pojo.MeetingPOJO;
import com.carbarn.meeting.pojo.dto.FinishedMeetingDTO;
import com.carbarn.meeting.pojo.dto.GoingToStartMeetingDTO;
import com.carbarn.meeting.pojo.dto.SearchMeetingDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingMapper {

    void createNewMeeting(MeetingPOJO meetingPOJO);

    void updateMeeting(MeetingPOJO meetingPOJO);

    MeetingPOJO getMeetingInfo(@Param("meeting_id") String meeting_id);

    void setMeetingTime(MeetingPOJO meetingPOJO);

    List<MeetingPOJO> getUserMeetings(@Param("user_id") long user_id,
                                      @Param("current_time") long current_time,
                                      @Param("searchMeetingDTO") SearchMeetingDTO searchMeetingDTO);

    List<GoingToStartMeetingDTO> isGoingToStartMeeting(@Param("currentTime") long currentTime,
                                                       @Param("time_interval") long time_interval);

    void updateInviteInfos(@Param("meeting_id") String meeting_id,
                           @Param("buyer_invite_infos") String buyer_invite_infos,
                           @Param("seller_invite_infos") String seller_invite_infos);

    List<FinishedMeetingDTO> isFinishedMeetings(@Param("currentTime") long currentTime,
                                                @Param("delete_minute_interval") long delete_minute_interval);

    void update_is_invite_delete(@Param("meeting_id") String meeting_id);
}
