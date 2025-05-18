package com.carbarn.meeting.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.meeting.pojo.MeetingPOJO;
import com.carbarn.meeting.pojo.dto.MeetingFullMessageDTO;
import com.carbarn.meeting.pojo.dto.SearchMeetingDTO;
import com.carbarn.meeting.service.MeetingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "视频会议服务")
@RestController
@RequestMapping("/carbarn/meeting")
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @PostMapping("/createNewMeeting")
    public AjaxResult createNewMeeting(@RequestHeader(name = "language", required = true) String language,
                                        @RequestBody MeetingPOJO meetingPOJO) {
        //TODO 1、通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        int car_id = meetingPOJO.getCar_id();
        long buyer_id = meetingPOJO.getBuyer_id();
        long seller_id = meetingPOJO.getSeller_id();
        if(car_id == 0 || buyer_id == 0 || seller_id == 0){
            return AjaxResult.error("error: Missing required parameter: 'car_id' or 'buyer_id' or 'seller_id'");
        }
        MeetingFullMessageDTO meetingFullMessageDTO = meetingService.createNewMeeting(language, meetingPOJO);
        return AjaxResult.success("create meeting success", meetingFullMessageDTO);
    }

    @PostMapping("/setMeetingTime")
    public AjaxResult setMeetingTime(@RequestHeader(name = "language", required = true) String language,
                                       @RequestBody MeetingPOJO meetingPOJO) {
        //TODO 1、通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        String meeting_id = meetingPOJO.getMeeting_id();
        long start_time = meetingPOJO.getStart_time();
        long end_time = meetingPOJO.getEnd_time();
        if(meeting_id == null || start_time == 0 || end_time == 0){
            return AjaxResult.error("error: Missing required parameter: 'meeting_id' or 'start_time' or 'end_time'");
        }
        meetingService.setMeetingTime(language, meetingPOJO);
        return AjaxResult.success("setMeetingTime success");
    }

    @PostMapping("/acceptMeetingInvite")
    public AjaxResult acceptMeetingInvite(@RequestHeader(name = "language", required = true) String language,
                                       @RequestBody MeetingPOJO meetingPOJO) {
        //TODO 1、通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        String meeting_id = meetingPOJO.getMeeting_id();
        meetingService.acceptMeetingInvite(meeting_id);
        return AjaxResult.success("acceptMeetingInvite success");
    }

    @PostMapping("/getMeetingInfo")
    public AjaxResult getMeetingInfo(@RequestHeader(name = "language", required = true) String language,
                                          @RequestBody MeetingPOJO meetingPOJO) {
        //TODO 1、通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        String meeting_id = meetingPOJO.getMeeting_id();
        MeetingFullMessageDTO meetingFullMessageDTO = meetingService.getMeetingInfo(language, meeting_id);
        return AjaxResult.success("getMeetingInfo success", meetingFullMessageDTO);
    }

    @PostMapping("/getUserMeetings")
    public AjaxResult getUserMeetings(@RequestHeader(name = "language", required = true) String language,
                                     @RequestBody SearchMeetingDTO searchMeetingDTO) {
        //TODO 1、通过satoken获取的用户id 和 contractPOJO中的buyer_id进行校验是否一致。
        int pageNo = searchMeetingDTO.getPageNo();
        int pageSize = searchMeetingDTO.getPageSize();
        if (pageNo < 1) {
            return AjaxResult.error("Missing required parameter: pageNo");
        }
        if (pageSize <= 0) {
            return AjaxResult.error("'pageSize' Must meet the conditions  pageSize > 0");
        } else {
            searchMeetingDTO.setPageStart((pageNo - 1) * pageSize);
        }

        String user_id = (String) StpUtil.getLoginId();
        List<MeetingPOJO> meetingPOJOS = meetingService.getUserMeetings(Long.valueOf(user_id), searchMeetingDTO);
        return AjaxResult.success("getUserMeetings success", meetingPOJOS);
    }


}
