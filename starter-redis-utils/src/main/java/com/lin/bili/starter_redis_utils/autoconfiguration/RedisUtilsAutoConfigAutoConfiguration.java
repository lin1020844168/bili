package com.lin.bili.starter_redis_utils.autoconfiguration;

import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration(proxyBeanMethods = false)
public class RedisUtilsAutoConfigAutoConfiguration {
    @Bean
    public RedisUtils redisUtils(StringRedisTemplate redisTemplate) {
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.setRedisTemplate(redisTemplate);
        return redisUtils;
    }
}
