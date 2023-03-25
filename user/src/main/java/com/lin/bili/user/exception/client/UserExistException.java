package com.lin.bili.user.exception.client;

import com.lin.bili.common.exception.ClientException;

/**
 * 用户已存在异常
 */
public class UserExistException extends ClientException {
    public UserExistException() {
        super("用户已存在");
    }
}
