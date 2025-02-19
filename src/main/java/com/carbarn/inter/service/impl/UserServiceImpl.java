package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    public AjaxResult signin(User user) {
        User existed_user = userMapper.selectByUsername(user.getUsername());
        if (existed_user == null) {

            try {
                userMapper.signin(user);
                return AjaxResult.success("注册成功");
            } catch (Exception e) {
                return AjaxResult.error("注册失败");
            }

        } else {
            return AjaxResult.error("用户名" + existed_user.getUsername() + "已经被注册过。");
        }


    }

    @Override
    public User findById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public UserPojo signup(SignupUserDTO signupUserDTO) {
        boolean bool = userMapper.isPhoneNumExist(signupUserDTO.getPhone_num());
        if(!bool){
            String nickname = "用户" + Utils.getRandomChar(10);
            signupUserDTO.setNickname(nickname);
            userMapper.signup(signupUserDTO);
        }

        UserPojo userPojo = userMapper.getUserInfoByPhoneNum(signupUserDTO.getPhone_num());
        return userPojo;
    }

    @Override
    public UserPojo vipsignup(VipSignupUserDTO vipSignupUserDTO) {
        userMapper.vipsignup(vipSignupUserDTO);
        UserPojo userPojo = userMapper.getUserInfoByID(vipSignupUserDTO.getId());
        return userPojo;
    }

    @Override
    public void updateNickname(long id, String nickname) {
        userMapper.updateNickname(id, nickname);
    }

    @Override
    public void updateAvatar(long id, String avatar) {
        userMapper.updateAvatar(id, avatar);
    }

    @Override
    public void updateCardealership(long id, String car_dealership) {
        userMapper.updateCardealership(id, car_dealership);
    }

    @Override
    public void updateAddress(long id, String address) {
        userMapper.updateAddress(id, address);
    }

    @Override
    public UserPojo getUserInfoByID(long userid) {
        return userMapper.getUserInfoByID(userid);
    }
}