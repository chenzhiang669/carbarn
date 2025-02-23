package com.carbarn.inter.config;

import com.carbarn.inter.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/carbarn/user/signup", "/carbarn/user/vipsignup", "/api/index",
                        "/carbarn/cars/search","/carbarn/cars/details", "/carbarn/cars/carTypeDetails","/carbarn/index/**",
                        "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**",
                        "/carbarn/feedback", "/carbarn/language", "/static/**", "/carbarn/pay/callback", "/carbarn/sms/**",
                        "/carbarn/event/**");
    }
}