package com.lin.bili.user.service.impl;

import com.lin.bili.user.UserApplication;
import com.lin.bili.user.dto.FriendInfoDto;
import com.lin.bili.user.mapper.UserFriendRMapper;
import com.lin.bili.user.mapper.UserMapper;
import com.lin.bili.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
class UserServiceImplTest {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    @Test
    void getFriendList() {
        List<FriendInfoDto> list = userMapper.getFriendInfoDtoByName("test");
        for (FriendInfoDto s : list) {
            System.out.println(s);
        }
    }

    @Test
    void getUser() {
        userService.addFriend("1", "1");
    }
}