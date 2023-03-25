package com.lin.bili.chat.strategy.impl;

import com.lin.bili.chat.po.TextMessage;
import com.lin.bili.chat.strategy.ParseMessageStrategy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ParseTextStrategy implements ParseMessageStrategy<TextMessage> {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public TextMessage parseMessage(String jsonStr) {
        return autoParseMessage(jsonStr, TextMessage.class);

    }
}
