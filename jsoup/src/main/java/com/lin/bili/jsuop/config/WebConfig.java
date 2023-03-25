package com.lin.bili.jsuop.config;

import com.lin.bili.common.constant.HttpContent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpEntity httpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, HttpContent.BILIBILI_COOKIE);
        headers.add(HttpHeaders.REFERER, HttpContent.BILIBILI_REFERER);
        return new HttpEntity(headers);
    }
}
