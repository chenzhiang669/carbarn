package com.carbarn.inter.service;


import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.event.EventPojo;
import com.carbarn.inter.pojo.user.dto.SignupUserDTO;
import com.carbarn.inter.pojo.user.dto.VipSignupUserDTO;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import com.carbarn.inter.utils.AjaxResult;
import org.apache.ibatis.annotations.Param;

public interface EventService {

    void updateViewCount(EventPojo eventPojo);

    void updateContactCount(EventPojo eventPojo);

}
