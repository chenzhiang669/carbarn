package com.carbarn.inter.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.carbarn.inter.utils.AjaxResult;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println(request.getRequestURI());
        if (StpUtil.isLogin()) {
            return true; // 校验登录状态
        }else{
            AjaxResult ajaxResult = AjaxResult.unlogin("unlogin");
            System.out.println(JSON.toJSONString(ajaxResult));
            response.getWriter().write(JSON.toJSONString(ajaxResult));
            return false;
        }
    }
}
