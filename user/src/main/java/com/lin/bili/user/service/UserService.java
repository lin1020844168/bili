package com.lin.bili.user.service;

import com.lin.bili.user.dto.FriendInfoDto;
import com.lin.bili.user.dto.UserInfoDto;
import com.lin.bili.user.exception.client.PasswordErrorException;
import com.lin.bili.user.po.User;

import java.util.List;

public interface UserService {
    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    UserInfoDto login(String account, String password) throws PasswordErrorException;

    /**
     * 发送验证码
     * @param phone
     */
    void sendCode(String phone);

    /**
     * 注册用户
     * @param user
     */
    void register(User user);

    /**
     * 判断用户昵称是否存在
     * @param name
     * @return
     */
    boolean existName(String name);

    /**
     * 判断用户账户是否存在
     * @param account
     * @return
     */
    boolean existAccount(String account);

    /**
     * 判断用户手机号码是否存在
     * @param phone
     * @return
     */
    boolean existPhone(String phone);

    /**
     * 用户注销
     */
    void logout(String token);

    UserInfoDto checkToken(String token);

    List<FriendInfoDto> findFriend(String name);

    FriendInfoDto getUser(String userId);

    void addFriend(String userId1, String userId2);

    List<FriendInfoDto> getFriendList(String userId);
}
