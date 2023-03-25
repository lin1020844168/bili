package com.lin.bili.common.constant;

import static com.lin.bili.common.constant.JWTConstant.PAYLOAD_USERDATA;

public class RedisConstant {
    //前缀
    public static final String SMS_CODE_PREFIX = "sms_code:";
    public static final String USERDATA_PREFIX = PAYLOAD_USERDATA+":";
    public static final String CHAT_SINGLE = "chat_single:";

    //key值
    public static final String HOT_WORD = "search_word";
}
