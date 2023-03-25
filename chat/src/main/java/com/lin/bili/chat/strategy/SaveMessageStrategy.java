package com.lin.bili.chat.strategy;

import com.lin.bili.chat.po.Message;

public interface SaveMessageStrategy<T extends Message> extends MessageStrategy<T> {
    void save(T message);
}
