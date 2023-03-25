package com.lin.bili.api.feign;

import com.lin.bili.api.dto.*;
import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.common.validation.annotation.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "user", path = "/user")
public interface UserFeign {
    @GetMapping("/anime/fav/get/{userId}")
    ResponseResult<List<AnimeFavDto>> getAnimeFav(@PathVariable("userId") Long userId);

    @PostMapping("/anime/fav/add/{userId}/{animeId}")
    ResponseResult<Boolean> addAnimeFav(@PathVariable("userId") Long userId, @PathVariable("animeId") Long animeId);

    @PostMapping("/anime/fav/del/{userId}/{animeId}")
    ResponseResult<Boolean> delAnimeFav(@PathVariable("userId") Long userId, @PathVariable("animeId") Long animeId);

    @GetMapping("/anime/history/get/{userId}")
    ResponseResult<List<AnimeHistoryDto>> getAnimeHistory(@PathVariable("userId") Long userId);

    @PostMapping("/anime/history/add/{userId}/{animeId}")
    ResponseResult<Boolean> addAnimeHistory(@PathVariable("userId") Long userId,
                                            @PathVariable("animeId") Long animeId);

    @PostMapping("/anime/history/del/{userId}/{animeId}")
    ResponseResult<Boolean> delAnimeHistory(@PathVariable("userId") Long userId,
                                            @PathVariable("animeId") Long animeId);

    @PostMapping("/anime/history/clear/{userId}")
    ResponseResult<Boolean> clearAnimeHistory(@PathVariable("userId") Long userId);

    @PostMapping("/anime/progress/add/{userId}/{episodeId}/{progress}")
    ResponseResult<Boolean> addEpisodeProgress(@PathVariable("userId") Long userId,
                                               @PathVariable("episodeId") Long episodeId,
                                               @PathVariable("progress") Double progress);

    @GetMapping("/anime/progress/get/{userId}")
    ResponseResult<List<PlayProgressDto>> getEpisodeProgress(@PathVariable("userId") Long userId);

    @GetMapping("/login")
    ResponseResult<UserInfoDto> login(@RequestParam("account") String account, @RequestParam("password")String password);

    @GetMapping("/checkToken/{token}")
    ResponseResult<UserInfoDto> checkToken(@PathVariable("token") String token);

    @PostMapping("/logout")
    ResponseResult logout();

    @PostMapping("/register")
    ResponseResult register(@RequestBody UserRegisterDto userRegisterDto);

    @PostMapping("/send/{phone}")
    ResponseResult sendCode(@PathVariable("phone") String phone);

    @GetMapping("/findFriend/{name}")
    ResponseResult<List<FriendInfoDto>> findFriend(@PathVariable("name") String name);

    @GetMapping("/getUser/{userId}")
    ResponseResult<FriendInfoDto> getUser(@PathVariable("userId")String userId);

    @GetMapping("/user/getFriendList/{userId}")
    ResponseResult<List<FriendInfoDto>> getFriendList(@PathVariable("userId") String userId);
}
