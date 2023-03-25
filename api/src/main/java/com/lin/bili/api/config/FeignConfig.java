package com.lin.bili.api.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            // 获取原请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            // 原请求 也就是http://order.gulimall.com/toTrade
            HttpServletRequest request = attributes.getRequest();
            // 获取原请求中携带的Cookie请求头
            String cookie = request.getHeader("Cookie");
            template.header("Cookie", cookie);
            String token = request.getHeader("token");
            template.header("token", token);
        };
    }

}
