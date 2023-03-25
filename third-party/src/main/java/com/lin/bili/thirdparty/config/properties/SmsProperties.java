package com.lin.bili.thirdparty.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aliyun.sms")
@Data
public class SmsProperties {
    public String accessKeyId;
    public String accessKeySecret;
}
