package com.carbarn.inter.service;


import com.carbarn.inter.pojo.User;

import java.util.List;

public interface UserService {
    User findById(Long id);

    List<User> findAll();

    void save(User user);

    void update(User user);

    void delete(Long id);
}
