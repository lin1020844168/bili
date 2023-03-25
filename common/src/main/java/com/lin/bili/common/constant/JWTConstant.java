package com.lin.bili.common.constant;

import java.time.Month;

public class JWTConstant {
    public static final String USERDATA_USERID = "userid";
    public static final String USERDATA_AUTHORITIES = "authorities";
    public static final String USERDATA_HEAD = "token";
    public static final String USERDATA_USERNAME = "username";
    public static final String USERDATA_IMG = "img";
    public static final String PAYLOAD_USERDATA = "userdata";

    //加密算法
    public static final String JWT_ALGORITHM = "SHA256";
    public static final String PUBLIC_KEY = "102010";

    public static final long SECOND = 1;
    public static final long MONTH = 30*3600*24;
    //过期时间
    public static final long JWT_EX = MONTH;
}
