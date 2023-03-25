package com.lin.bili.user.controller;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.user.dto.AnimeFavDto;
import com.lin.bili.user.service.AnimeFavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/anime/fav")
public class AnimeFavController {
    @Autowired
    private AnimeFavService addAnimeFav;

    @PostMapping("/add/{userId}/{animeId}")
    public ResponseResult<Boolean> addAnimeFav(@PathVariable("userId") Long userId, @PathVariable("animeId") Long animeId) {
        addAnimeFav.addAnimeFav(userId, animeId);
        return ResponseResult.success(true);
    }

    @PostMapping("/del/{userId}/{animeId}")
    public ResponseResult<Boolean> delAnimeFav(@PathVariable("userId") Long userId, @PathVariable("animeId") Long animeId) {
        addAnimeFav.delAnimeFav(userId, animeId);
        return ResponseResult.success(true);
    }

    @GetMapping("/get/{userId}")
    public ResponseResult<List<AnimeFavDto>> getAnimeFav(@PathVariable("userId") Long userId) {
        List<AnimeFavDto> animeFavDtoList = addAnimeFav.getAnimeFav(userId);
        return ResponseResult.success(animeFavDtoList);
    }
}
