package com.lin.bili.thirdparty.service.impl;

import com.aliyun.teaopenapi.Client;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.google.gson.Gson;
import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.thirdparty.service.AliyunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AliyunServiceImpl implements AliyunService {
    @Autowired
    private IAcsClient client;

    @Override
    public ResponseResult sendCode(String phoneNumber, String code) {
        SendSmsRequest request = new SendSmsRequest();
        request.setSignName("阿里云短信测试");
        request.setTemplateCode("SMS_154950909");
        request.setPhoneNumbers(phoneNumber);
        request.setTemplateParam("{\"code\":\""+code+"\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
            return ResponseResult.failure();

        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            return ResponseResult.failure();
        }
        return ResponseResult.success();
    }
}