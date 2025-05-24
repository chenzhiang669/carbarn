package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ParamsMapper paramsMapper;

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
        String phone_number = signupUserDTO.getPhone_num();
        int phone_number_length = phone_number.length();
        String phone_number_last4num = phone_number.substring(phone_number_length - 4, phone_number_length);
        boolean bool = userMapper.isPhoneNumExist(signupUserDTO.getPhone_num(), signupUserDTO.getArea_code());
        if (!bool) {

            Random random = new Random();
            String default_avatars = paramsMapper.getValue(ParamKeys.param_user_default_avatar);
            String[] default_avatars_arrays = default_avatars.split(",");
            int random_index = random.nextInt(default_avatars_arrays.length);
            signupUserDTO.setAvatar(default_avatars_arrays[random_index]);


            String default_nicknames = paramsMapper.getValue(ParamKeys.param_user_default_nickname);
            String[] default_nicknames_arrays = default_nicknames.split(",");
            random_index = random.nextInt(default_nicknames_arrays.length);
            String nickname = default_nicknames_arrays[random_index];
            signupUserDTO.setNickname(nickname + phone_number_last4num);


            long user_count = Utils.getRandomLong();
            signupUserDTO.setUser_count(user_count);
//            String nickname = "用户" + Utils.getRandomChar(10);
//            signupUserDTO.setNickname(nickname);

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

    @Override
    public void updateUserInfo(UserPojo userPojo) {
        userMapper.updateUserInfo(userPojo);
    }

    @Override
    public void updateLanguage(UserPojo userPojo) {
        userMapper.updateLanguage(userPojo);
    }

    @Override
    public AjaxResult viewCount(long user_id) {
        LocalDate today_ = LocalDate.now();
        LocalDate yesterday_ = today_.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = today_.format(formatter);
        String yesterday = yesterday_.format(formatter);

        UserViewCountPojo total_count = userMapper.userViewCount(user_id);
        if (total_count == null) {
            total_count = new UserViewCountPojo();
        }

        UserViewCountPojo today_count = userMapper.userDtViewCount(user_id, today);
        if (today_count == null) {
            today_count = new UserViewCountPojo();
        }
        UserViewCountPojo yesterday_count = userMapper.userDtViewCount(user_id, yesterday);

        if (yesterday_count == null) {
            yesterday_count = new UserViewCountPojo();
        }

        JSONObject json = new JSONObject();
        JSONObject view_count_json = new JSONObject();
        JSONObject viewed_count_json = new JSONObject();
        JSONObject contact_count_json = new JSONObject();
        JSONObject contacted_count_json = new JSONObject();
        view_count_json.put("total", total_count.getView_count());
        viewed_count_json.put("total", total_count.getViewed_count());
        contact_count_json.put("total", total_count.getContact_count());
        contacted_count_json.put("total", total_count.getContacted_count());

        view_count_json.put("today", today_count.getView_count());
        viewed_count_json.put("today", today_count.getViewed_count());
        contact_count_json.put("today", today_count.getContact_count());
        contacted_count_json.put("today", today_count.getContacted_count());

        view_count_json.put("yesterday", yesterday_count.getView_count());
        viewed_count_json.put("yesterday", yesterday_count.getViewed_count());
        contact_count_json.put("yesterday", yesterday_count.getContact_count());
        contacted_count_json.put("yesterday", yesterday_count.getContacted_count());

        json.put("view_count", view_count_json);
        json.put("viewed_count", viewed_count_json);
        json.put("contact_count", contact_count_json);
        json.put("contacted_count", contacted_count_json);

        return AjaxResult.success("查询浏览量数据成功", json);
    }

    //注销用户
    @Override
    public void deRegister(long user_id) {
        userMapper.deRegister(user_id);
    }
}