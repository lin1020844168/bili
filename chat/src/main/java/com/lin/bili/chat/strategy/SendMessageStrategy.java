package com.lin.bili.chat.strategy;

import com.lin.bili.chat.po.Message;

public interface SendMessageStrategy<T extends Message> extends MessageStrategy<T> {
    void sendToClient(T message);
}
