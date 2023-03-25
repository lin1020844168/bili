package com.lin.bili.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * 网关全局CORS配置<br/>
 * 注：通过spring.cloud.gateway.globalcors配置并未生效，故使用此代码配置。<br/>
 * 后续升级SCG版本可再测试下是否好用。<br/>
 * 该类仅用作临时开发，后续若使用此实现需将设置项提取成配置属性。
 *
 * @author luohq
 * @date 2022-06-10
 *
 */
@Configuration
public class GatewayConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        //允许cookies跨域
//        config.setAllowCredentials(true);
        //允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedOrigin("*");
        //允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        //预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        //允许提交请求的方法类型，*表示全部允许
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");

        org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource source =
                new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}

