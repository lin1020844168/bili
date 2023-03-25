package com.lin.bili.user.controller;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.user.service.AnimeProgressService;
import com.lin.bili.user.dto.PlayProgressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/anime/progress")
@CrossOrigin
public class AnimeProgressController {
    @Autowired
    private AnimeProgressService animeService;

    @PostMapping("/add/{userId}/{episodeId}/{progress}")
    public ResponseResult<Boolean> addEpisodeProgress(@PathVariable("userId") Long userId,
                                                      @PathVariable("episodeId") Long episodeId,
                                                      @PathVariable("progress") Double progress) {
        animeService.addEpisodeProgress(userId, episodeId, progress);
        return ResponseResult.success(true);
    }

    @GetMapping("/get/{userId}")
    public ResponseResult<List<PlayProgressDto>> getEpisodeProgress(@PathVariable("userId") Long userId) {
        List<PlayProgressDto> playProgressList =  animeService.getEpisodeProgress(userId);
        return ResponseResult.success(playProgressList);
    }

}
