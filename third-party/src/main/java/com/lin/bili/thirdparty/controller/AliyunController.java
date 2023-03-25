package com.lin.bili.thirdparty.controller;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.thirdparty.service.AliyunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AliyunController {
    @Autowired
    private AliyunService aliyunService;

    @PostMapping("/sms/{phone}/{code}")
    public ResponseResult sendCode(@PathVariable("phone") String phoneNumber,@PathVariable("code") String code) {
        return aliyunService.sendCode(phoneNumber, code);
    }
}
