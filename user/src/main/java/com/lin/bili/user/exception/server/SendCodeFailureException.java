package com.lin.bili.user.exception.server;

import com.lin.bili.common.exception.WarnServerException;

public class SendCodeFailureException extends WarnServerException {
    public SendCodeFailureException() {
        super("发送验证码失败");
    }
}
