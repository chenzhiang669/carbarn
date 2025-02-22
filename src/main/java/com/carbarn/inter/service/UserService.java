package com.carbarn.inter.service;


import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import com.carbarn.inter.utils.AjaxResult;
import org.apache.ibatis.annotations.Param;

public interface UserService {

    User selectByUsername(String username);


    AjaxResult signin(User user);

    User findById(Long userId);


    UserPojo signup(SignupUserDTO signupUserDTO);

    UserPojo vipsignup(VipSignupUserDTO vipSignupUserDTO);

    void updateNickname(long id, String nickname);

    void updateAvatar(long id, String avatar);

    void updateCardealership(long id, String car_dealership);

    void updateAddress(long id, String address);

    UserPojo getUserInfoByID(long userid);

    void updateUserInfo(UserPojo userPojo);

    AjaxResult viewCount(long user_id);
}
