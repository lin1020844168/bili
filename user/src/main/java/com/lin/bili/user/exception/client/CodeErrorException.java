package com.lin.bili.user.exception.client;

import com.lin.bili.common.exception.ClientException;

/**
 * 验证码错误异常
 */
public class CodeErrorException extends ClientException {
    public CodeErrorException() {
        super("验证码错误");
    }
}
