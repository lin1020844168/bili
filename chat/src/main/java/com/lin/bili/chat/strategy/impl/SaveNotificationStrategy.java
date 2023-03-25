package com.lin.bili.chat.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.lin.bili.chat.po.NotificationMessage;
import com.lin.bili.chat.strategy.SaveMessageStrategy;
import com.lin.bili.common.constant.RabbitmqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SaveNotificationStrategy implements SaveMessageStrategy<NotificationMessage> {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void save(NotificationMessage message) {
        String messageJsonStr = JSONUtil.parse(message).toString();
        rabbitTemplate.convertSendAndReceive(RabbitmqConstant.NOTIFICATION_MESSAGE_EXCHANGE,
                RabbitmqConstant.NOTIFICATION_MESSAGE_ROUTING, messageJsonStr);
    }
}
