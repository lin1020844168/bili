package com.lin.bili.user.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.lin.bili.common.constant.RedisConstant;
import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.common.validation.annotation.Phone;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import com.lin.bili.user.dto.FriendInfoDto;
import com.lin.bili.user.dto.UserInfoDto;
import com.lin.bili.user.exception.client.*;
import com.lin.bili.user.po.User;
import com.lin.bili.user.service.UserService;
import com.lin.bili.user.dto.UserRegisterDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lin.bili.common.constant.JWTConstant.USERDATA_HEAD;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/search/history")
    public ResponseResult searchHistory() {
        return null;
    }

    @RequestMapping("/test")
    public ResponseResult<Map<String, String>> test() {
        Map<String, String> data = new HashMap<>();
        data.put("code", "1");
        data.put("state", "1");
        return ResponseResult.success(data);
    }

    @GetMapping("/findFriend/{name}")
    public ResponseResult<List<FriendInfoDto>> findFriend(@PathVariable("name") String name) {
        List<FriendInfoDto> friendInfoDtoList = userService.findFriend(name);
        List<FriendInfoDto> filter = friendInfoDtoList.stream().filter(e ->
                !e.getUserId().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
                .collect(Collectors.toList());
        return ResponseResult.success(filter);
    }

    @GetMapping("/getUser/{userId}")
    public ResponseResult<FriendInfoDto> getUser(@PathVariable("userId")String userId) {
        FriendInfoDto friendInfoDto = userService.getUser(userId);
        return ResponseResult.success(friendInfoDto);
    }


    @GetMapping("/login")
    public ResponseResult<UserInfoDto> login(@RequestParam("account") String account, @RequestParam("password")String password) throws PasswordErrorException {
        UserInfoDto userInfoDto = userService.login(account, password);
        return ResponseResult.success(userInfoDto);
    }

    @GetMapping("/checkToken/{token}")
    public ResponseResult<UserInfoDto> checkToken(@PathVariable("token") String token) {
        UserInfoDto userInfoDto = userService.checkToken(token);
        return ResponseResult.success(userInfoDto);
    }

    @PostMapping("/logout")
    public ResponseResult logout(HttpServletRequest request) {
        String token = request.getHeader(USERDATA_HEAD);
        userService.logout(token);
        return ResponseResult.success();
    }

    @GetMapping("/exist/name/{name}")
    public ResponseResult<Boolean> existName(@PathVariable("name") String name) {
        boolean res = userService.existName(name);
        return ResponseResult.success(res);
    }

    @GetMapping("/exist/account/{account}")
    public ResponseResult<Boolean> existAccount(@PathVariable("account") String account) {
        boolean res = userService.existAccount(account);
        return ResponseResult.success(res);
    }

    @GetMapping("exist/phone/{phone}")
    public ResponseResult<Boolean> existPhone(@PathVariable("phone") String phone) {
        boolean res = userService.existPhone(phone);
        return ResponseResult.success(res);
    }

    @PostMapping("/register")
    public ResponseResult register(@Valid @RequestBody UserRegisterDto userRegisterDto) throws CodeErrorException, UserExistException, PhoneExistException {
        String smsCode = redisUtils.get(RedisConstant.SMS_CODE_PREFIX + userRegisterDto.getPhoneNumber());
        if (smsCode==null || !smsCode.equals(userRegisterDto.getSmsCode())) {
            throw new CodeErrorException();
        }
        if (existName(userRegisterDto.getName()).getData()) {
            throw new UserExistException();
        }
        if (existAccount(userRegisterDto.getPhoneNumber()).getData()) {
            throw new PhoneExistException();
        }
        User user = new User();
        BeanUtils.copyProperties(userRegisterDto, user);
        String password = user.getPassword();
        String pw_hash = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(pw_hash);
        long id = IdUtil.getSnowflakeNextId();
        user.setId(id);
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        userService.register(user);
        return ResponseResult.success();
    }

    @PostMapping("/send/{phone}")
    public ResponseResult sendCode(@Phone @PathVariable("phone") String phone) throws CodeRepetitionException {
        if (existPhone(phone).getData()) {
            throw new PhoneExistException();
        }
        if (redisUtils.hasKey(RedisConstant.SMS_CODE_PREFIX+phone)) {
            throw new CodeRepetitionException();
        }
        userService.sendCode(phone);
        return ResponseResult.success();
    }

    @PostMapping("/user/addFriend/{userId1}/{userId2}")
    public ResponseResult<Void> addFriend(@PathVariable("userId1")String userId1, @PathVariable("userId2")String userId2) {
        userService.addFriend(userId1, userId2);
        return ResponseResult.success();
    }

    @GetMapping("/user/getFriendList/{userId}")
    public ResponseResult<List<FriendInfoDto>> getFriendList(@PathVariable("userId") String userId) {
        List<FriendInfoDto> friendInfoDtoList = userService.getFriendList(userId);
        return ResponseResult.success(friendInfoDtoList);
    }

}
