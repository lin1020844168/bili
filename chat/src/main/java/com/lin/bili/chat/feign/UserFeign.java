package com.lin.bili.chat.feign;

import com.lin.bili.common.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "user", path = "/user")
public interface UserFeign {
    @PostMapping("/user/addFriend/{userId1}/{userId2}")
    ResponseResult<Void> addFriend(@PathVariable("userId1")String userId1, @PathVariable("userId2")String userId2);
}
