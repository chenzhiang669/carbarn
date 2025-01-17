package com.carbarn.inter.service;


import com.carbarn.inter.pojo.User;
import com.carbarn.inter.utils.AjaxResult;

public interface UserService {

    User selectByUsername(String username);


    AjaxResult signin(User user);

    User findById(Long userId);
}
