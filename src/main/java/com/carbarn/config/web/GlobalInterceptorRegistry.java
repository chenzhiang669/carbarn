package com.carbarn.config.web;

import com.carbarn.common.interceptor.TraceIdInterceptor;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GlobalInterceptorRegistry implements WebMvcConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalInterceptorRegistry.class);
    private static final List<String> EXCLUDE_PATH_PATTERNS = Lists.newArrayList(
            "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/actuator/*",
            "/auth/doLogin", "/auth/isRegister"
    );
    @Resource
    private HttpServletRequest request;

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
//        registry.addInterceptor(new SaInterceptor(handle ->
//                {
//                    if (!StpUtil.isLogin()) {
//                        LOGGER.info("intercept unLogin uri: {}", request.getRequestURI());
//                    }
//                    StpUtil.checkLogin();
//                }))
//                .addPathPatterns("/**")
//                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);

        registry.addInterceptor(new TraceIdInterceptor())
                .excludePathPatterns("/**");


    }

    private List<String> addPath(String... paths) {
        List<String> list = Lists.newArrayListWithCapacity(EXCLUDE_PATH_PATTERNS.size() + paths.length);
        list.addAll(EXCLUDE_PATH_PATTERNS);
        list.addAll(Arrays.asList(paths));
        return list;
    }

    private List<String> deletePath(String... paths) {
        List<String> list = Lists.newArrayListWithCapacity(EXCLUDE_PATH_PATTERNS.size());
        list.addAll(EXCLUDE_PATH_PATTERNS);
        for (String path : paths) {
            list.remove(path);
        }
        return list;
    }

    /**
     * 跨域支持
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 对接口配置跨域设置
        registry.addMapping("/**")
                // 设置访问源地址
                .allowedOrigins("*")
                .allowCredentials(true)
                // 设置访问源请求方法
                .allowedMethods("*")
                // 设置访问源请求头
                .allowedHeaders("*")
                .maxAge(3600 * 24);
    }
}
