package com.lin.bili.chat.config;

import com.lin.bili.chat.constant.HttpConstant;
import com.lin.bili.chat.server.WebSocketHandler;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            String token = HttpConstant.NETTY_TOKEN;
            template.header("token", token);
        };
    }
}
