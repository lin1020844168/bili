package com.lin.bili.chat.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.lin.bili.chat.po.Message;
import com.lin.bili.chat.strategy.SaveMessageStrategy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.lin.bili.common.constant.RabbitmqConstant.CHAT_MESSAGE_EXCHANGE;
import static com.lin.bili.common.constant.RabbitmqConstant.CHAT_MESSAGE_ROUTING;

@Component
public class SaveTextStrategy implements SaveMessageStrategy {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void save(Message message) {
        String messageJsonStr = JSONUtil.parse(message).toString();
        rabbitTemplate.convertSendAndReceive(CHAT_MESSAGE_EXCHANGE, CHAT_MESSAGE_ROUTING, message);
    }
}
