package com.carbarn.inter.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.service.impl.UserServiceImpl;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserServiceImpl userService;

    // 登录接口
    @PostMapping("/login")
    public AjaxResult login(@RequestBody User user) {
        User dbUser = userService.selectByUsername(user.getUsername());
        if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
            StpUtil.login(dbUser.getId());
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("satoken", StpUtil.getTokenValue());
            map.put("id", dbUser.getId());
            return AjaxResult.success("登录成功", map);
        }
        return AjaxResult.error("用户名或密码错误");
    }


    // 登录接口
    @PostMapping("/signin")
    public AjaxResult signin(@RequestBody User user) {
        if(user.getUsername() == null || user.getPassword() == null){
            return AjaxResult.error("密码或账户为空");
        }

        return userService.signin(user);
    }

}
