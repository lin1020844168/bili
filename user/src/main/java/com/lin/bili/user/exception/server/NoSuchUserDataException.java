package com.lin.bili.user.exception.server;

import com.lin.bili.common.exception.WarnServerException;

public class NoSuchUserDataException extends WarnServerException {
    public NoSuchUserDataException() {
        super("没有找到该用户相关数据");
    }
}
