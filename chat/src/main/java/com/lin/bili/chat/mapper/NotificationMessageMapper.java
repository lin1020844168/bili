package com.lin.bili.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.chat.po.Message;
import com.lin.bili.chat.po.NotificationMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface NotificationMessageMapper extends BaseMapper<NotificationMessage> {
    void insertNotificationMessage(NotificationMessage notificationMessage);
    void updateAgreeStateById(@Param("id")Long id, @Param("agreeState") Integer agreeState);
}
