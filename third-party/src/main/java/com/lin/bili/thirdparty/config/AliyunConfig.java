package com.lin.bili.thirdparty.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.lin.bili.thirdparty.config.properties.SmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class AliyunConfig {
    @Bean
    public IAcsClient sms(SmsProperties smsProperties) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsProperties.accessKeyId, smsProperties.accessKeySecret);
        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         "<your-access-key-id>",       // The AccessKey ID of the RAM account
         "<your-access-key-secret>",   // The AccessKey Secret of the RAM account
         "<your-sts-token>");          // STS Token
         **/
        System.out.println(smsProperties);
        IAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
