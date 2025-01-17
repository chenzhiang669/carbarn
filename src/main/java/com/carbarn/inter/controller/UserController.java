package com.carbarn.inter.controller;

import com.carbarn.inter.pojo.User;
import com.carbarn.inter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

}
