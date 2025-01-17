package com.carbarn.inter.helper;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.carbarn.common.exception.ServiceException;
import com.carbarn.common.exception.enums.ErrorCode;
import com.carbarn.common.utils.ObjectJsonMapper;
import com.carbarn.inter.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zoulingxi
 * @description 获取当前登录用户类
 * @date 2025/1/11 17:03
 */
public class UserHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserHelper.class);

    public static User nowLoginUser() {
        try {
            String userJson = StpUtil.getSession().getString(SaSession.USER);
            return ObjectJsonMapper.fromJSON(userJson, User.class);
        } catch (Exception e) {
            LOGGER.error("获取当前登录用户失败", e);
            throw new ServiceException(ErrorCode.FAIL_AUTH);
        }
    }

}