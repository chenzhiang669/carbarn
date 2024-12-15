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

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/")
    public void createUser(@RequestBody User user) {
        userService.save(user);
    }

    @PutMapping("/")
    public void updateUser(@RequestBody User user) {
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
