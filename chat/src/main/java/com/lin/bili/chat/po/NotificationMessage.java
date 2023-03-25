package com.lin.bili.chat.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 *
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-11-01 15:41:26
 */
@Data
@NoArgsConstructor
public class NotificationMessage extends Message {
    private static final long serialVersionUID = 1L;

    public NotificationMessage(Long id, Long toUserId, Long fromUserId, Integer method, Integer type, Date sendTime, int agreeState, String token) {
        super(id, toUserId, fromUserId, method, type, sendTime, token);
        this.agreeState = agreeState;
    }

    //0-未接受 1 接受 2拒绝
    private Integer agreeState;


}
