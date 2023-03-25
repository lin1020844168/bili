package com.lin.bili.api.controller;

import cn.hutool.json.JSONUtil;
import cn.hutool.system.UserInfo;
import com.lin.bili.api.dto.FriendInfoDto;
import com.lin.bili.api.dto.UserRegisterDto;
import com.lin.bili.api.feign.UserFeign;
import com.lin.bili.api.dto.UserInfoDto;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserFeign userFeign;

    @GetMapping("/getUser/{userId}")
    public ResponseResult<FriendInfoDto> getUser(@PathVariable("userId")String userId) {
        return userFeign.getUser(userId);
    }


    @GetMapping("/login/{account}/{password}")
    public ResponseResult<UserInfoDto> login(@PathVariable("account") String account,
                                        @PathVariable("password") String password) {
        ResponseResult<UserInfoDto> resp = userFeign.login(account, password);
        return resp;
    }

    @GetMapping("/checkToken/{token}")
    public ResponseResult<UserInfoDto> checkToken(@PathVariable("token") String token) {
        ResponseResult<UserInfoDto> resp = userFeign.checkToken(token);
        return resp;
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return userFeign.logout();
    }

    @PostMapping("/register")
//    phone:string, password:string, name: string, code:string
    public ResponseResult register(@RequestBody Map<String, String> map) {

        String phone = map.get("phone");
        String password = map.get("password");
        String name = map.get("name");
        String code = map.get("code");
        return userFeign.register(new UserRegisterDto(name, phone, password, phone, code));
    }


    @GetMapping("/getCode/{phone}")
    public ResponseResult getCode(@PathVariable("phone") String phone) {
        return userFeign.sendCode(phone);
    }

    @GetMapping("/findFriend/{name}")
    ResponseResult<List<FriendInfoDto>> findFriend(@PathVariable("name") String name) {
        return userFeign.findFriend(name);
    }

    @GetMapping("/getFriendList/{name}")
    public ResponseResult<List<FriendInfoDto>> getFriendList(@PathVariable("name") String name) {
        return userFeign.getFriendList(name);
    }


}
