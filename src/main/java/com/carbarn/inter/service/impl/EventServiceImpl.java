package com.carbarn.inter.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.carbarn.inter.mapper.EventMapper;
import com.carbarn.inter.mapper.UserMapper;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.event.EventPojo;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.pojo.user.pojo.UserViewCountPojo;
import com.carbarn.inter.service.EventService;
import com.carbarn.inter.service.UserService;
import com.carbarn.inter.utils.AjaxResult;
import com.carbarn.inter.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventMapper eventMapper;


    @Override
    public void updateViewCount(EventPojo eventPojo) {
        LocalDate today_ = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dt = today_.format(formatter);
        if(eventPojo.getBuyer_id() != -1){
            eventMapper.insertViewCount(eventPojo.getBuyer_id(), dt);
        }

        if(eventPojo.getSeller_id() != -1){
            eventMapper.insertViewedCount(eventPojo.getSeller_id(), dt);
        }

    }

    @Override
    public void updateContactCount(EventPojo eventPojo) {
        LocalDate today_ = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dt = today_.format(formatter);
        if(eventPojo.getBuyer_id() != -1){
            eventMapper.insertContactCount(eventPojo.getBuyer_id(), dt);
        }

        if(eventPojo.getSeller_id() != -1){
            eventMapper.insertContactedCount(eventPojo.getSeller_id(), dt);
        }
    }
}