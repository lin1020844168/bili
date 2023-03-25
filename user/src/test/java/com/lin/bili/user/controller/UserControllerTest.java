package com.lin.bili.user.controller;

import com.lin.bili.common.constant.RedisConstant;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import com.lin.bili.user.UserApplication;
import com.lin.bili.user.dto.FriendInfoDto;
import com.lin.bili.user.dto.UserRegisterDto;
import com.lin.bili.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
class UserControllerTest {
    @Autowired
    UserController userController;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    UserService userService;

    @Test
    void test() {
        List<FriendInfoDto> friendList = userService.getFriendList("1583559470876315648");
        friendList.forEach(System.out::println);
    }

    @Test
    void register() {
        Map<String, String> map = new HashMap<>();
        for (int i=10; i<=30; i++) {
            String phone = "155219456"+i;
            String name = "test"+i;
            String password = "102010";
            userController.sendCode(phone);
            if (redisUtils.hasKey(RedisConstant.SMS_CODE_PREFIX+phone)) {
                String code = redisUtils.get(RedisConstant.SMS_CODE_PREFIX + phone);
                UserRegisterDto userRegisterDto = new UserRegisterDto(name, phone, password, phone, code);
                userController.register(userRegisterDto);
            }

        }
    }
}