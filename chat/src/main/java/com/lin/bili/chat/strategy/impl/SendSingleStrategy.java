package com.lin.bili.chat.strategy.impl;

import com.lin.bili.chat.po.Message;
import com.lin.bili.chat.po.ReturnMessage;
import com.lin.bili.chat.server.WebSocketHandler;
import com.lin.bili.chat.strategy.SendMessageStrategy;
import com.lin.bili.common.utils.JJsonUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;



@Component
public class SendSingleStrategy implements SendMessageStrategy {
    @Override
    public void sendToClient(Message message) {
        Long toUserId = message.getToUserId();
        Long fromUserId = message.getFromUserId();
        boolean isOnline = WebSocketHandler.channelMapThreadLocal.get().containsKey(toUserId);
        String messageJsonStr = JJsonUtils.parse(message);
        Channel channelTo = WebSocketHandler.channelMapThreadLocal.get().get(toUserId);
        Channel channelFrom = WebSocketHandler.channelMapThreadLocal.get().get(fromUserId);
        if (isOnline) {
            channelTo.writeAndFlush(new TextWebSocketFrame(messageJsonStr));
        }
        String successReturn = ReturnMessage.success(message.getToken(), message.getType());
        channelFrom.writeAndFlush(new TextWebSocketFrame(successReturn));
    }
}
