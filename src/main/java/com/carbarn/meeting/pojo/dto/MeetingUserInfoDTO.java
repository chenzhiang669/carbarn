package com.carbarn.meeting.pojo.dto;

import com.carbarn.inter.pojo.user.pojo.UserPojo;
import lombok.Data;

@Data
public class MeetingUserInfoDTO {
    private long id;

    private String nickname;

    private String phone_num;

    private String avatar;

    private String address;

    private String car_dealership;

    public static MeetingUserInfoDTO getMeetingBuyerInfoDTO(UserPojo userPojo) {
        MeetingUserInfoDTO meetingUserInfoDTO = new MeetingUserInfoDTO();
        meetingUserInfoDTO.setId(userPojo.getId());
        meetingUserInfoDTO.setNickname(userPojo.getNickname());
        meetingUserInfoDTO.setPhone_num(userPojo.getPhone_num());
        meetingUserInfoDTO.setAvatar(userPojo.getAvatar());
        meetingUserInfoDTO.setAddress(userPojo.getAddress());
        meetingUserInfoDTO.setCar_dealership(userPojo.getCar_dealership());
        return meetingUserInfoDTO;
    }
}
