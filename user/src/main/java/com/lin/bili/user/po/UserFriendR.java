package com.lin.bili.user.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_friend_r")
public class UserFriendR {
    /**
     * 用户id
     */
    private Long userId;

    private Long userFriendId;
}
