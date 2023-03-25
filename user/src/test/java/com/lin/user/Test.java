package com.lin.user;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import com.lin.bili.user.UserApplication;
import com.lin.bili.user.dto.FriendInfoDto;
import com.lin.bili.user.mapper.UserMapper;
import com.lin.bili.user.po.User;
import com.lin.bili.user.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lin.bili.common.constant.JWTConstant.*;
import static com.lin.bili.common.constant.JWTConstant.JWT_EX;
import static com.lin.bili.common.constant.RedisConstant.USERDATA_PREFIX;

@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;

    @org.junit.Test
    public void test() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("102010");
        System.out.println(encode);
    }

    @org.junit.Test
    public void test1()  {
        FriendInfoDto lin = userMapper.getFriendInfoDtoByName("lin").get(0);
        for (int i = 10; i<=30; i++) {
            String name = "test"+i;
            FriendInfoDto friend = userMapper.getFriendInfoDtoByName(name).get(0);
            userService.addFriend(lin.getUserId(), friend.getUserId());
            userService.addFriend(friend.getUserId(), lin.getUserId());
        }
    }
}
