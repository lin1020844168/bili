package com.lin.bili.user.exception.client;

import com.lin.bili.common.exception.ClientException;

/**
 * 手机号已被注册异常
 */
public class PhoneExistException extends ClientException {
    public PhoneExistException() {
        super("手机号已被注册");
    }
}
