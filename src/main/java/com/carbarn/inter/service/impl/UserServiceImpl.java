package com.carbarn.inter.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carbarn.contract.mapper.ContractMapper;
import com.carbarn.inter.config.ParamKeys;
import com.carbarn.inter.mapper.CarsMapper;
import com.carbarn.inter.mapper.ParamsMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.sms.CheckVerifyCodeDTO;
import com.carbarn.inter.pojo.user.dto.*;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import com.carbarn.inter.service.EmailService;
import com.carbarn.inter.service.SmsService;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ParamsMapper paramsMapper;

    @Autowired
    private CarsMapper carsMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private SmsService smsService;

    @Autowired
    EmailService emailService;

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
        UserPojo userPojo = userMapper.getUserInfoByPhoneNum(signupUserDTO.getPhone_num(), signupUserDTO.getArea_code());
        if (userPojo == null) {

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

        userPojo = userMapper.getUserInfoByPhoneNum(signupUserDTO.getPhone_num(), signupUserDTO.getArea_code());
        return userPojo;
    }

    @Override
    public AjaxResult signupEmail(SignupUserDTO signupUserDTO) {
        String email = signupUserDTO.getEmail();
        String veri_code = signupUserDTO.getVeri_code();
        CheckVerifyCodeDTO checkVerifyCodeDTO = new CheckVerifyCodeDTO();
        checkVerifyCodeDTO.setEmail(email);
        checkVerifyCodeDTO.setVeri_code(veri_code);
        boolean bool = emailService.checkVerifyCode(checkVerifyCodeDTO);
        if(!bool){
            return AjaxResult.error("Incorrect verification code");
        }

        UserPojo userPojo = userMapper.getUserInfoByEmail(email);
        if (userPojo == null) {
            Random random = new Random();
            String default_avatars = paramsMapper.getValue(ParamKeys.param_user_default_avatar);
            String[] default_avatars_arrays = default_avatars.split(",");
            int random_index = random.nextInt(default_avatars_arrays.length);
            signupUserDTO.setAvatar(default_avatars_arrays[random_index]);
            signupUserDTO.setNickname(email);

            long user_count = Utils.getRandomLong();
            signupUserDTO.setUser_count(user_count);
            userMapper.signup(signupUserDTO);
        }

        userPojo = userMapper.getUserInfoByEmail(email);
        StpUtil.login(userPojo.getId());
        userPojo.setSatoken(StpUtil.getTokenValue());
        SaSession session = StpUtil.getSession();
        session.set(SaSession.USER, JSON.toJSONString(userPojo));

        return AjaxResult.success("注册用户成功", userPojo);
    }

    @Override
    public AjaxResult signupPhoneNum(SignupUserDTO signupUserDTO) {
        String phone_num = signupUserDTO.getPhone_num();
        String verify_code = signupUserDTO.getVeri_code();
        String area_code = signupUserDTO.getArea_code();
        if(phone_num.startsWith("+86")){
            signupUserDTO.setArea_code("+86");
            signupUserDTO.setPhone_num(phone_num.substring(3));
        }else if(area_code == null && !phone_num.startsWith("+86")){
            signupUserDTO.setArea_code("+86");
        }

        CheckVerifyCodeDTO checkVerifyCodeDTO = new CheckVerifyCodeDTO();
        checkVerifyCodeDTO.setPhone_num(phone_num);
        checkVerifyCodeDTO.setVeri_code(verify_code);
        checkVerifyCodeDTO.setArea_code(signupUserDTO.getArea_code());
        boolean bool = smsService.checkVerifyCode(checkVerifyCodeDTO);
        if(!bool){
            return AjaxResult.error("验证码错误");
        }

        int phone_number_length = phone_num.length();
        String phone_number_last4num = phone_num.substring(phone_number_length - 4, phone_number_length);
        UserPojo userPojo = userMapper.getUserInfoByPhoneNum(signupUserDTO.getPhone_num(), signupUserDTO.getArea_code());
        if (userPojo == null) {

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
            userMapper.signup(signupUserDTO);
        }

        userPojo = userMapper.getUserInfoByPhoneNum(signupUserDTO.getPhone_num(), signupUserDTO.getArea_code());
        StpUtil.login(userPojo.getId());
        userPojo.setSatoken(StpUtil.getTokenValue());
        SaSession session = StpUtil.getSession();
        session.set(SaSession.USER, JSON.toJSONString(userPojo));
        return AjaxResult.success("注册用户成功", userPojo);

    }

    @Override
    public UserPojo vipsignup(VipSignupUserDTO vipSignupUserDTO) {
        userMapper.vipsignup(vipSignupUserDTO);
        UserPojo userPojo = userMapper.getUserInfoByID(vipSignupUserDTO.getId());
        return userPojo;
    }

    @Override
    public AjaxResult subvipsignup(SubVipSignupUserDTO subVipSignupUserDTO) {
        String phone_number = subVipSignupUserDTO.getPhone_num();
        int phone_number_length = phone_number.length();
        String phone_number_last4num = phone_number.substring(phone_number_length - 4, phone_number_length);

        UserPojo userPojo = userMapper.getUserInfoByPhoneNum(subVipSignupUserDTO.getPhone_num(), subVipSignupUserDTO.getArea_code());
        if (userPojo == null) {

            Random random = new Random();
            String default_avatars = paramsMapper.getValue(ParamKeys.param_user_default_avatar);
            String[] default_avatars_arrays = default_avatars.split(",");
            int random_index = random.nextInt(default_avatars_arrays.length);
            subVipSignupUserDTO.setAvatar(default_avatars_arrays[random_index]);


            String default_nicknames = paramsMapper.getValue(ParamKeys.param_user_default_nickname);
            String[] default_nicknames_arrays = default_nicknames.split(",");
            random_index = random.nextInt(default_nicknames_arrays.length);
            String nickname = default_nicknames_arrays[random_index];
            subVipSignupUserDTO.setNickname(nickname + phone_number_last4num);


            long user_count = Utils.getRandomLong();
            subVipSignupUserDTO.setUser_count(user_count);

            userMapper.subvipsignup(subVipSignupUserDTO);

            userPojo = userMapper.getUserInfoByPhoneNum(subVipSignupUserDTO.getPhone_num(), subVipSignupUserDTO.getArea_code());
        }else if(userPojo.getRole() == 1){
            return AjaxResult.error("号码为门店管理员，无法加入门店");
        }else if(userPojo.getRole() == 0){
            userMapper.updateParentId(subVipSignupUserDTO);
            userPojo = userMapper.getUserInfoByPhoneNum(subVipSignupUserDTO.getPhone_num(), subVipSignupUserDTO.getArea_code());
        }

        return AjaxResult.success("注册子账户成功", userPojo);
    }

    @Override
    public List<SubUsersDTO> subUsers(Long user_id) {
        List<SubUsersDTO> subUsersDTOS = userMapper.getSubUsers(user_id);
        return subUsersDTOS;
    }

    @Override
    public int getMaxNumberOfSubUsers() {
        try{
            String subuser_max_number = paramsMapper.getValue(ParamKeys.subuser_max_number);
            if(subuser_max_number == null){
                return 5;
            }else {
                return Integer.valueOf(subuser_max_number);
            }
        }catch (Exception e){
            return 5;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteSubUsers(long parent_id, DeleteSubUsersDTO subUsersDTO) throws Exception {

        long sub_user_id = subUsersDTO.getSub_user_id();

        UserPojo sub_userpojo = userMapper.getUserInfoByID(sub_user_id);
        if(sub_userpojo == null){
            return AjaxResult.error("当前子账户不存在");
        }

        if(parent_id != sub_userpojo.getParent_id()){
            return AjaxResult.error("子账户不属于当前管理员，无法进行删除!");
        }

        // 1. 汽车权限归属更改
        carsMapper.updateUserId(sub_user_id, parent_id);

        // 2. 合同权限归属更改
        contractMapper.updateSellerId(sub_user_id, parent_id);

        // 3. 标记用户为删除
        userMapper.deRegister(sub_user_id);

        return AjaxResult.success("子账户删除成功，所有数据权限已转移给管理员!");
    }

    @Override
    public AjaxResult transferSubUser(long parent_id, TransferSubUsersDTO transferSubUsersDTO) {

        CheckVerifyCodeDTO checkVerifyCodeDTO = new CheckVerifyCodeDTO();
        checkVerifyCodeDTO.setPhone_num(transferSubUsersDTO.getTarget_phone_num());
        checkVerifyCodeDTO.setArea_code(transferSubUsersDTO.getTarget_area_code());
        checkVerifyCodeDTO.setVeri_code(transferSubUsersDTO.getVeri_code());
        boolean bool = smsService.checkVerifyCode(checkVerifyCodeDTO);
        if(!bool){
            return AjaxResult.error("验证码错误");
        }

        String original_phone_num = transferSubUsersDTO.getOriginal_phone_num();
        String original_area_code = transferSubUsersDTO.getOriginal_area_code();

        UserPojo original_sub_user_pojo = userMapper.getUserInfoByPhoneNum(original_phone_num, original_area_code);
        if(original_sub_user_pojo == null){
            return AjaxResult.error("原子账户不存在，无法进行子账户变更!");
        }

        if(parent_id != original_sub_user_pojo.getParent_id()){
            return AjaxResult.error("原子账户不属于当前管理员，无法进行子账户变更!");
        }

        String target_phone_num = transferSubUsersDTO.getTarget_phone_num();
        String target_area_code = transferSubUsersDTO.getTarget_area_code();
        UserPojo target_sub_user_pojo = userMapper.getUserInfoByPhoneNum(target_phone_num, target_area_code);
        if(target_sub_user_pojo != null){
            return AjaxResult.error("新手机号已经被注册过，无法进行子账户变更!");
        }


        long original_user_id = original_sub_user_pojo.getId();
        userMapper.transferSubUser(target_phone_num, target_area_code, original_user_id);

        return AjaxResult.success("子账户变更成功!");

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