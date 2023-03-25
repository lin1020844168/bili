package com.lin.bili.chat.strategy.context;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lin.bili.chat.po.Message;
import com.lin.bili.chat.strategy.ParseMessageStrategy;
import com.lin.bili.chat.strategy.SaveMessageStrategy;
import com.lin.bili.chat.strategy.SendMessageStrategy;
import com.lin.bili.chat.strategy.impl.*;
import com.lin.bili.common.strategy.StrategyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.lin.bili.chat.constant.MessageType.NOTIFICATION;
import static com.lin.bili.chat.constant.MessageType.TEXT;
import static com.lin.bili.chat.constant.SendMethod.SINGLE;

@Component
@Scope("prototype")
public class MessageStrategyContext extends StrategyContext {
    @Autowired
    private ParseTextStrategy parseTextStrategy;

    @Autowired
    private ParseNotificationStrategy parseNotificationStrategy;

    @Autowired
    private SendSingleStrategy sendSingleStrategy;

    @Autowired
    private SaveNotificationStrategy saveNotificationStrategy;

    @Autowired
    private SaveTextStrategy saveTextStrategy;


    private ParseMessageStrategy parseMessageStrategy;
    private SendMessageStrategy sendMessageStrategy;
    private SaveMessageStrategy saveMessageStrategy;

    public MessageStrategyContext() {
    }

    public void sendMessage(String messageJsonStr) {
        Message message = parseMessageStrategy.parseMessage(messageJsonStr);
        sendMessageStrategy.sendToClient(message);
        saveMessageStrategy.save(message);
    }

    public MessageStrategyContext chooseStrategy(String messageJsonStr) {
        JSONObject messageJson = JSONUtil.parseObj(messageJsonStr);
        Integer type = messageJson.get("type", Integer.class);
        Integer method = messageJson.get("method", Integer.class);
        if (type== NOTIFICATION) {
            this.parseMessageStrategy = parseNotificationStrategy;
            this.saveMessageStrategy = saveNotificationStrategy;
        } else if (type == TEXT) {
            this.parseMessageStrategy = parseTextStrategy;
            this.saveMessageStrategy = saveTextStrategy;
        }
        if (method==SINGLE) {
            this.sendMessageStrategy = sendSingleStrategy;
        }
        return this;
    }
}
