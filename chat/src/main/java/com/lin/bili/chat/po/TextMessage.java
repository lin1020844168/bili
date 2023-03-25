package com.lin.bili.chat.po;

import lombok.Data;

import java.util.Date;

@Data
public class TextMessage extends Message {
    private static final long serialVersionUID = 1L;

    public TextMessage(Long id, Long toUserId, Long fromUserId, Integer method, Integer type, Date sendTime, boolean isRead, String content, String token) {
        super(id, toUserId, fromUserId, method, type, sendTime, token);
        this.isRead = isRead;
        this.content = content;
    }

    /**
     * 已读状态
     */
    private boolean isRead;

    private String content;
}
