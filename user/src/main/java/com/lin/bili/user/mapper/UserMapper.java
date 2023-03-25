package com.lin.bili.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.user.dto.FriendInfoDto;
import com.lin.bili.user.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserDao
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<FriendInfoDto> getFriendInfoDtoByName(@Param("name") String name);

    FriendInfoDto getFriendInfoDtoByUserId(@Param("id") Long userId);

    List<FriendInfoDto> listFriendInfoByUserId(long parseLong);
}
