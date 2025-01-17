package com.carbarn.inter.service.impl;

import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
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
}