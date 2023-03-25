package com.lin.bili.user.exception.client;

import com.lin.bili.common.exception.ClientException;

/**
 * 用户名或密码错误异常
 */
public class PasswordErrorException extends ClientException {
    public PasswordErrorException() {
        super("用户名或密码错误");
    }
}
