package com.carbarn.inter.pojo.dto.cars;

import com.carbarn.inter.pojo.user.pojo.UserPojo;
import lombok.Data;

@Data
public class CarUserInfoDTO {

    private String address;
    private String nickname;
    private String phone_num;
    private String real_name;
    private String avatar;
    private String car_dealership;

    public static CarUserInfoDTO getCarUserInfoDTO(UserPojo userPojo) {
        CarUserInfoDTO carUserInfoDTO = new CarUserInfoDTO();
        carUserInfoDTO.setAddress(userPojo.getAddress());
        carUserInfoDTO.setNickname(userPojo.getNickname());
        carUserInfoDTO.setPhone_num(userPojo.getPhone_num());
        carUserInfoDTO.setReal_name(userPojo.getReal_name());
        carUserInfoDTO.setAvatar(userPojo.getAvatar());
        carUserInfoDTO.setCar_dealership(userPojo.getCar_dealership());
        return carUserInfoDTO;
    }
}
