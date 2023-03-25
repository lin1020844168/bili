package com.lin.bili.common.utils;

import cn.hutool.jwt.JWT;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.lin.bili.common.constant.JWTConstant.JWT_EX;
import static com.lin.bili.common.constant.JWTConstant.PUBLIC_KEY;

public class TokenUtils {
    public static String updateToken(String token) {
        JWT jwt = JWT.of(token);
        long currentTime = new Date().getTime();
        long expireTime = currentTime+JWT_EX*1000;
        String updateToken = jwt.setExpiresAt(new Date(expireTime)).setKey(PUBLIC_KEY.getBytes(StandardCharsets.UTF_8)).sign();
        return updateToken;
    }

    public static boolean checkToken(String token) {
        return JWT.of(token).setKey(PUBLIC_KEY.getBytes(StandardCharsets.UTF_8)).verify();
    }
}
