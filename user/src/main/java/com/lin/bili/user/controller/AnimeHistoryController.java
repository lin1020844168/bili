package com.lin.bili.user.controller;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.user.dto.AnimeHistoryDto;
import com.lin.bili.user.service.AnimeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/anime/history")
public class AnimeHistoryController {
    @Autowired
    private AnimeHistoryService animeHistoryService;

    @GetMapping("/get/{userId}")
    public ResponseResult<List<AnimeHistoryDto>> getAnimeHistory(@PathVariable("userId") Long userId) {
        List<AnimeHistoryDto> animeHistoryDtoList = animeHistoryService.getAnimeHistory(userId);
        return ResponseResult.success(animeHistoryDtoList);
    }

    @PostMapping("/add/{userId}/{animeId}")
    public ResponseResult<Boolean> addAnimeHistory(@PathVariable("userId") Long userId,
                                                   @PathVariable("animeId") Long animeId) {
        animeHistoryService.addAnimeHistory(userId, animeId);
        return ResponseResult.success(true);
    }

    @PostMapping("/del/{userId}/{animeId}")
    public ResponseResult<Boolean> delAnimeHistory(@PathVariable("userId") Long userId,
                                                   @PathVariable("animeId") Long animeId) {
        animeHistoryService.delAnimeHistory(userId, animeId);
        return ResponseResult.success(true);
    }

    @PostMapping("/clear/{userId}")
    public ResponseResult<Boolean> clearAnimeHistory(@PathVariable("userId") Long userId) {
        animeHistoryService.clearAnimeHistory(userId);
        return ResponseResult.success(true);
    }
}
