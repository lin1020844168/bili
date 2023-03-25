package com.lin.bili.user.exception.server;

import com.lin.bili.common.exception.WarnServerException;

public class JwtExpireException extends WarnServerException {
    public JwtExpireException() {
        super("jwt已过期");
    }
}
