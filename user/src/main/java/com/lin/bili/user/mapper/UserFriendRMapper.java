package com.lin.bili.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.user.po.UserFriendR;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFriendRMapper extends BaseMapper<UserFriendR> {
    void selectByUserId(Long parseLong);
}
