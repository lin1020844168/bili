package com.lin.bili.thirdparty.service;

import com.lin.bili.common.utils.ResponseResult;

public interface AliyunService {
    ResponseResult sendCode(String phoneNumber, String code);
}
