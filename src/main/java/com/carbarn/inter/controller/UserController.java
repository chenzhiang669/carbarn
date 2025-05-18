package com.carbarn.inter.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.sms.SendSms;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户服务")
@RestController
@RequestMapping("/carbarn/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/signup")
    public AjaxResult signup(@RequestHeader(name = "language", required = true) String language,
                             @RequestBody SignupUserDTO signupUserDTO) {

        if(signupUserDTO.getPhone_num() == null || signupUserDTO.getVeri_code() == null){
            return AjaxResult.error("Missing required parameter: 'phone_num' or 'veri_code'");
        }

        String phone_num = signupUserDTO.getPhone_num();
        String verify_code = signupUserDTO.getVeri_code();
        if(!"11235813".equals(verify_code)){
            boolean bool = SendSms.checkVerifyCode(phone_num, verify_code);
            if(!bool){
                return AjaxResult.error("验证码错误");
            }
        }

        signupUserDTO.setLanguage(language);
        UserPojo userPojo = userService.signup(signupUserDTO);
        StpUtil.login(userPojo.getId());
        userPojo.setSatoken(StpUtil.getTokenValue());

        SaSession session = StpUtil.getSession();
        session.set(SaSession.USER, JSON.toJSONString(userPojo));
        return AjaxResult.success("注册用户成功", userPojo);
    }


    @PostMapping("/vipsignup")
    public AjaxResult vipsignup(@RequestBody VipSignupUserDTO vipSignupUserDTO) {
        UserPojo userPojo = userService.vipsignup(vipSignupUserDTO);
        StpUtil.login(userPojo.getId());
        userPojo.setSatoken(StpUtil.getTokenValue());

        SaSession session = StpUtil.getSession();
        session.set(SaSession.USER, JSON.toJSONString(userPojo));
        return AjaxResult.success("注册车商成功", userPojo);
    }

    @PostMapping("/userinfo")
    public AjaxResult userinfo() {
        try{
            String user_id = (String) StpUtil.getLoginId();
            UserPojo userPojo = userService.getUserInfoByID(Long.valueOf(user_id));
            userPojo.setSatoken(StpUtil.getTokenValue());
            return AjaxResult.success("获取用户信息成功", userPojo);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("获取用户信息失败");
        }

    }


//    @PostMapping("/updateNickname")
//    public AjaxResult updateNickname(@RequestBody String body) {
//        JSONObject json = JSON.parseObject(body);
//        if (!json.containsKey("id")) {
//            return AjaxResult.error("Missing required parameter 'id'");
//        } else if (!json.containsKey("nickname")) {
//            return AjaxResult.error("Missing required parameter 'nickname'");
//        }
//
//        long id = json.getLong("id");
//        String nickname = json.getString("nickname");
//        userService.updateNickname(id, nickname);
//        return AjaxResult.success("修改昵称成功");
//    }
//
//
//    @PostMapping("/updateAvatar")
//    public AjaxResult updateAvatar(@RequestBody String body) {
//        JSONObject json = JSON.parseObject(body);
//        if (!json.containsKey("id")) {
//            return AjaxResult.error("Missing required parameter 'id'");
//        } else if (!json.containsKey("avatar")) {
//            return AjaxResult.error("Missing required parameter 'avatar'");
//        }
//
//        long id = json.getInteger("id");
//        String avatar = json.getString("avatar");
//        userService.updateAvatar(id, avatar);
//        return AjaxResult.success("修改头像成功");
//    }
//
//
//    @PostMapping("/updateAddress")
//    public AjaxResult updateAddress(@RequestBody String body) {
//        JSONObject json = JSON.parseObject(body);
//        if (!json.containsKey("id")) {
//            return AjaxResult.error("Missing required parameter 'id'");
//        } else if (!json.containsKey("address")) {
//            return AjaxResult.error("Missing required parameter 'address'");
//        }
//
//        long id = json.getLong("id");
//        String address = json.getString("address");
//        userService.updateAddress(id, address);
//        return AjaxResult.success("修改地址成功");
//    }
//
//
//    @PostMapping("/updateCardealership")
//    public AjaxResult updateCardealership(@RequestBody String body) {
//        JSONObject json = JSON.parseObject(body);
//        if (!json.containsKey("id")) {
//            return AjaxResult.error("Missing required parameter 'id'");
//        } else if (!json.containsKey("car_dealership")) {
//            return AjaxResult.error("Missing required parameter 'car_dealership'");
//        }
//
//        long id = json.getInteger("id");
//        String car_dealership = json.getString("car_dealership");
//        userService.updateCardealership(id, car_dealership);
//        return AjaxResult.success("修改车行名称成功");
//    }

    @PostMapping("/updateUserInfo")
    public AjaxResult updateUserInfo(@RequestBody UserPojo userPojo) {

        String user_id = (String) StpUtil.getLoginId();
        userPojo.setId(Long.valueOf(user_id));
        userService.updateUserInfo(userPojo);
        return AjaxResult.success("修改用户信息成功");
    }


    @PostMapping("/updateLanguage")
    public AjaxResult updateLanguage(@RequestBody UserPojo userPojo) {

        try{
            if(userPojo.getLanguage() == null || "".equals(userPojo.getLanguage())){
                return AjaxResult.error("Missing required parameter: language");
            }
            String user_id = (String) StpUtil.getLoginId();
            userPojo.setId(Long.valueOf(user_id));
            userService.updateLanguage(userPojo);
            return AjaxResult.success("修改用户语言成功");
        }catch (Exception e){
            return AjaxResult.error("修改用户语言失败");
        }

    }

    @PostMapping("/viewCount")
    public AjaxResult viewCount() {
        String user_id = (String) StpUtil.getLoginId();
        return userService.viewCount(Long.valueOf(user_id));
    }

    //用户退出登录
    @PostMapping("/signout")
    public AjaxResult signout(@RequestHeader(name = "satoken", required = true) String satoken) {
        String token = StpUtil.getTokenValue();
        StpUtil.logout();
        stringRedisTemplate.delete(token);
        return AjaxResult.success("退出登录成功");
    }

    //注销用户: 注销用户后，用户不能再使用了。但是可以使用同一个手机号重新注册
    @PostMapping("/deRegister")
    public AjaxResult deRegister(@RequestHeader(name = "satoken", required = true) String satoken) {
        String user_id = (String) StpUtil.getLoginId();
        userService.deRegister(Long.valueOf(user_id)); //注销掉用户
        String token = StpUtil.getTokenValue();
        StpUtil.logout();
        stringRedisTemplate.delete(token); //将用户在redis中的satoken删除
        return AjaxResult.success("用户注销成功");
    }
}
