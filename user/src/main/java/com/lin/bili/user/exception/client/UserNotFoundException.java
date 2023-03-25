package com.lin.bili.user.exception.client;

import com.lin.bili.common.exception.ClientException;
import com.lin.bili.common.exception.WarnServerException;

public class UserNotFoundException extends ClientException {
    public UserNotFoundException() {
        super("找不到该用户");
    }
}
