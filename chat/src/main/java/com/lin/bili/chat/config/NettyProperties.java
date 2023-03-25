package com.lin.bili.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "netty.config")
@Data
public class NettyProperties {
    public int port = 8888;
}
