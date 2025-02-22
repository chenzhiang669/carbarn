package com.carbarn.common.interceptor;


import com.carbarn.common.utils.TraceIdUtils;
import com.carbarn.inter.helper.UserHelper;
import com.carbarn.inter.pojo.User;
import com.carbarn.inter.pojo.user.pojo.UserPojo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@code @Description} 为http请求添加traceId
 *
 * @Author zoulingxi
 * @Date 2023/4/7 14:33
 */
public class TraceIdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        if (DispatcherType.ASYNC == request.getDispatcherType()) {
            return true;
        }
        String header = request.getHeader(TraceIdUtils.TRACE_KEY);
        UserPojo user = UserHelper.nowLoginUser();
        String userId = user == null ? null : String.valueOf(user.getId());
        if (StringUtils.isEmpty(header)) {
            TraceIdUtils.genAndSetTraceId(userId);
        } else {
            TraceIdUtils.setTraceId(header, userId);
        }
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                           @NonNull Object handler, @Nullable ModelAndView modelAndView) {
        TraceIdUtils.clear();
    }
}
