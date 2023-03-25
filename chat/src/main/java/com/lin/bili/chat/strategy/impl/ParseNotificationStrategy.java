package com.lin.bili.chat.strategy.impl;

import com.lin.bili.chat.po.NotificationMessage;
import com.lin.bili.chat.strategy.ParseMessageStrategy;
import org.springframework.stereotype.Component;

@Component
public class ParseNotificationStrategy implements ParseMessageStrategy<NotificationMessage> {
    @Override
    public NotificationMessage parseMessage(String messageJsonStr) {
        return autoParseMessage(messageJsonStr, NotificationMessage.class);
    }
}
