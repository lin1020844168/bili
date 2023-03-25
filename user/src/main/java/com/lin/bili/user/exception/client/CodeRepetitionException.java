package com.lin.bili.user.exception.client;

import com.lin.bili.common.exception.ClientException;

/**
 * 重复获取验证码异常
 */
public class CodeRepetitionException extends ClientException {
    public CodeRepetitionException() {
        super("重复获取验证码");
    }
}
