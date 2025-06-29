package com.carbarn.inter.service;


import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.dto.*;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import com.carbarn.inter.utils.AjaxResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    void updateLanguage(UserPojo userPojo);

    AjaxResult viewCount(long user_id);

    void deRegister(long user_id);

    AjaxResult subvipsignup(SubVipSignupUserDTO subVipSignupUserDTO);

    List<SubUsersDTO> subUsers(Long user_id);

    int getMaxNumberOfSubUsers();

    AjaxResult deleteSubUsers(long valueOf, DeleteSubUsersDTO subUsersDTO) throws Exception;

    AjaxResult transferSubUser(long valueOf, TransferSubUsersDTO transferSubUsersDTO);

    AjaxResult signupEmail(SignupUserDTO signupUserDTO);

    AjaxResult signupPhoneNum(SignupUserDTO signupUserDTO);
}
