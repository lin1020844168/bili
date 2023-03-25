package com.lin.bili.chat.strategy;

import cn.hutool.json.JSONUtil;
import com.lin.bili.chat.po.Message;


public interface ParseMessageStrategy<T extends Message> extends MessageStrategy<T> {
    T parseMessage(String messageJsonStr);

    default T autoParseMessage(String messageJsonStr, Class<T> tClass) {
        return JSONUtil.toBean(messageJsonStr, tClass);
    }
}
