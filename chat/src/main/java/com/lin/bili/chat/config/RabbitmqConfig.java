package com.lin.bili.chat.config;

import com.lin.bili.common.constant.RabbitmqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
@Slf4j
public class RabbitmqConfig {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Bean
    public Queue chatMessageQueue() {
        return QueueBuilder.durable(RabbitmqConstant.CHAT_MESSAGE_QUEUE).build();
    }

    @Bean
    public Exchange chatMessageExchange() {
        return ExchangeBuilder.directExchange(RabbitmqConstant.CHAT_MESSAGE_EXCHANGE).build();
    }

    @Bean
    public Binding chatMessageBinding() {
        return BindingBuilder.bind(chatMessageQueue()).to(chatMessageExchange()).with(RabbitmqConstant.CHAT_MESSAGE_ROUTING).noargs();
    }

    @Bean
    public Queue notificationMessageQueue() {
        return QueueBuilder.durable(RabbitmqConstant.NOTIFICATION_MESSAGE_QUEUE).build();
    }

    @Bean
    public Exchange notificationMessageExchange() {
        return ExchangeBuilder.directExchange(RabbitmqConstant.NOTIFICATION_MESSAGE_EXCHANGE).build();
    }

    @Bean
    public Binding notificationMessageRouting() {
        return BindingBuilder.bind(chatMessageQueue()).to(chatMessageExchange()).with(RabbitmqConstant.NOTIFICATION_MESSAGE_ROUTING).noargs();
    }

    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.info("消息不可达, 消息回退");
        });
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.info("消息未确认，发送失败");
            }
        });
        rabbitTemplate.setMandatory(true);
    }
}
