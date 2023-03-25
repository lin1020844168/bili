package com.lin.bili.chat.listener;

import cn.hutool.json.JSONUtil;
import com.lin.bili.chat.feign.UserFeign;
import com.lin.bili.chat.mapper.NotificationMessageMapper;
import com.lin.bili.chat.po.NotificationMessage;
import com.lin.bili.chat.po.TextMessage;
import com.lin.bili.common.constant.RabbitmqConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.lin.bili.chat.constant.AgreeState.AGREE;
import static com.lin.bili.chat.constant.AgreeState.NOOPERATION;

@Component
public class MessageListener {
    @Autowired
    private NotificationMessageMapper notificationMessageMapper;

    @Autowired
    private UserFeign userFeign;

    @RabbitListener(queues = RabbitmqConstant.CHAT_MESSAGE_QUEUE, ackMode = "MANUAL")
    public void messageListener(Message message, Channel channel) throws IOException {
        try {
            String messageJsonStr = new String(message.getBody(), StandardCharsets.UTF_8);
            TextMessage textMessage = JSONUtil.toBean(messageJsonStr, TextMessage.class);
            System.out.println(textMessage);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            e.printStackTrace();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    @RabbitListener(queues = RabbitmqConstant.NOTIFICATION_MESSAGE_QUEUE, ackMode = "MANUAL")
    @Transactional
    public void notificationListener(Message message, Channel channel) throws IOException {
        String messageJsonStr = new String(message.getBody(), StandardCharsets.UTF_8);
        NotificationMessage notificationMessage = JSONUtil.toBean(messageJsonStr, NotificationMessage.class);
        if (notificationMessage.getAgreeState()== NOOPERATION) {
            notificationMessageMapper.insertNotificationMessage(notificationMessage);
        } else {
            notificationMessageMapper.updateAgreeStateById(notificationMessage.getId(),
                    notificationMessage.getAgreeState());
            if (notificationMessage.getAgreeState()==AGREE) {
                userFeign.addFriend(notificationMessage.getToUserId().toString(),
                        notificationMessage.getFromUserId().toString());
                userFeign.addFriend(notificationMessage.getFromUserId().toString(),
                        notificationMessage.getToUserId().toString());
            }
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
    }
}
