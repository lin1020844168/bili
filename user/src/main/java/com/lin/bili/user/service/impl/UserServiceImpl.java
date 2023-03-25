package com.lin.bili.user.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.bili.common.constant.JWTConstant;
import com.lin.bili.common.constant.RedisConstant;
import com.lin.bili.common.utils.TokenUtils;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import com.lin.bili.user.dto.FriendInfoDto;
import com.lin.bili.user.dto.UserInfoDto;
import com.lin.bili.user.exception.client.PasswordErrorException;
import com.lin.bili.user.exception.client.UserNotFoundException;
import com.lin.bili.user.exception.server.JwtExpireException;
import com.lin.bili.user.exception.server.NoSuchUserDataException;
import com.lin.bili.user.feign.ThirdPartyFeign;
import com.lin.bili.user.mapper.UserFriendRMapper;
import com.lin.bili.user.mapper.UserMapper;
import com.lin.bili.user.po.User;
import com.lin.bili.user.po.UserFriendR;
import com.lin.bili.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lin.bili.common.constant.JWTConstant.*;
import static com.lin.bili.common.constant.RedisConstant.USERDATA_PREFIX;

/**
 * 处理用户逻辑业务类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ThirdPartyFeign thirdPartyFeign;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserFriendRMapper userFriendRMapper;


    /**
     * 登录功能实现
     * @param account
     * @param password
     * @return
     */
    @Override
    public UserInfoDto login(String account, String password) throws PasswordErrorException {
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(account, password));
        } catch (BadCredentialsException e) {
            throw new PasswordErrorException();
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException();
        }
        String userId = authenticate.getName();
        User user = userMapper.selectById(userId);
        Map<String, Object> userData = new HashMap<>();
        userData.put(USERDATA_USERID, userId);
        userData.put(USERDATA_USERNAME, user.getName());
        userData.put(USERDATA_IMG, user.getImage());
        List<String> authorities = new ArrayList<>();
        userData.put(USERDATA_AUTHORITIES, authorities);
        long currentTime = new Date().getTime();
        long expireTime = currentTime+JWT_EX*1000;
        String userDataKey = USERDATA_PREFIX +IdUtil.randomUUID();
        JWT jwt = JWT.create().setPayload(PAYLOAD_USERDATA, userDataKey).setExpiresAt(new Date(expireTime));
        String value = JSONUtil.parse(userData).toString();
        String token = jwt.setKey(PUBLIC_KEY.getBytes(StandardCharsets.UTF_8)).sign();
        redisUtils.setEx(userDataKey, value, JWT_EX, TimeUnit.SECONDS);
        UserInfoDto userInfoDto = new UserInfoDto(userId, user.getName(), user.getImage(), token);
        return userInfoDto;
    }

    /**
     * 调用阿里云服务，发送验证码
     * ps：短信要钱生成后到redis看
     * @param phone
     */
    @Override
    public void sendCode(String phone) {
        int smsCode = (int)(Math.random()*1000000);
        redisUtils.setEx(RedisConstant.SMS_CODE_PREFIX+phone, smsCode+"", 60, TimeUnit.SECONDS);
//        ResponseResult responseResult = thirdPartyFeign.sendCode(phone, smsCode + "");
//        if (responseResult.getCode()== HttpCode.FAILURE.getCode()) {
//            throw new SendCodeFailureException();
//        }
    }

    /**
     * 注册用户信息
     * @param user
     */
    @Override
    public void register(User user) {
        userMapper.insert(user);
    }

    /**
     * 判断用户名称是否存在
     * @param name
     * @return
     */
    @Override
    public boolean existName(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", name);
        User user = userMapper.selectOne(queryWrapper);
        return user!=null;
    }

    /**
     * 判断用户账号是否存在
     * @param account
     * @return
     */
    @Override
    public boolean existAccount(String account) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account", account);
        User user = userMapper.selectOne(queryWrapper);
        return user!=null;
    }

    /**
     * 判断用户手机号码是否存在
     * @param phone
     * @return
     */
    @Override
    public boolean existPhone(String phone) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone_number", phone);
        User user = userMapper.selectOne(queryWrapper);
        return user!=null;
    }

    /**
     * 用户注销
     */
    @Override
    public void logout(String token) {
        JWT jwt = JWT.of(token);
        String userdataKey = jwt.getPayloads().get(PAYLOAD_USERDATA).toString();
        redisUtils.delete(userdataKey);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userid = authentication.getName();
        SecurityContextHolder.clearContext();
    }

    @Override
    public UserInfoDto checkToken(String token) {
        JWT jwt = JWT.of(token);
        if (!jwt.setKey(PUBLIC_KEY.getBytes(StandardCharsets.UTF_8)).validate(0)) {
            throw new JwtExpireException();
        }
        JSONObject payloads = jwt.getPayloads();
        System.out.println(new Date(Long.parseLong(payloads.get("exp").toString())) );
        String userDataKey = payloads.get(PAYLOAD_USERDATA).toString();
        if (!redisUtils.hasKey(userDataKey)) {
            throw new NoSuchUserDataException();
        }
        String userDataJson = redisUtils.get(userDataKey);
        JSONObject userData = JSONUtil.parseObj(userDataJson);
        String username= userData.get(USERDATA_USERNAME).toString();
        String userId = userData.get(USERDATA_USERID).toString();
        String img = userData.get(JWTConstant.USERDATA_AUTHORITIES).toString();
        String updateToken = TokenUtils.updateToken(token);
        redisUtils.expire(userDataKey, JWT_EX,TimeUnit.SECONDS);
        UserInfoDto userInfoDto = new UserInfoDto(userId, username, img, updateToken);
        return userInfoDto;
    }

    @Override
    public List<FriendInfoDto> findFriend(String name) {
        return userMapper.getFriendInfoDtoByName(name);
    }

    @Override
    public FriendInfoDto getUser(String userId) {
        return userMapper.getFriendInfoDtoByUserId(Long.parseLong(userId));
    }

    @Override
    public void addFriend(String userId1, String userId2) {
        userFriendRMapper.insert(new UserFriendR(Long.parseLong(userId1), Long.parseLong(userId2)));
    }

    @Override
    public List<FriendInfoDto> getFriendList(String userId) {
        return userMapper.listFriendInfoByUserId(Long.parseLong(userId));
    }

}
