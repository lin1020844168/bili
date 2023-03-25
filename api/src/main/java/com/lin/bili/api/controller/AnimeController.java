package com.lin.bili.api.controller;

import com.lin.bili.api.dto.*;
import com.lin.bili.api.exception.server.EpisodeNotfoundException;
import com.lin.bili.api.feign.AnimeFeign;
import com.lin.bili.api.feign.SearchFeign;
import com.lin.bili.api.feign.UserFeign;
import com.lin.bili.api.vo.AnimeFavVo;
import com.lin.bili.api.vo.AnimeHistoryVo;
import com.lin.bili.api.vo.PlayProgressVo;
import com.lin.bili.common.utils.HttpCode;
import com.lin.bili.common.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/anime")
public class AnimeController {
    @Autowired
    private UserFeign userFeign;

    @Autowired
    private AnimeFeign animeFeign;

    @Autowired
    private SearchFeign searchFeign;

    @GetMapping("/fav/get/{id}")
    public ResponseResult<List<AnimeFavVo>> getAnimeFav(@PathVariable("id") Long id) {
        ResponseResult<List<AnimeFavDto>> data = userFeign.getAnimeFav(id);
        if (data.getCode()!= HttpCode.SUCCESS.getCode()) {
            return ResponseResult.failure(data.getMessage());
        }
        List<AnimeFavDto> animeFavDtoList = data.getData();
        List<AnimeFavVo> animeFavVoList = new ArrayList<>();
        animeFavDtoList.forEach(e -> {
            BaseAnimeDto baseAnimeDto = animeFeign.getBaseAnime(e.getAnimeId()).getData();
            animeFavVoList.add(new AnimeFavVo(e.getAnimeId().toString(), baseAnimeDto.getTitle(),
                    baseAnimeDto.getCover(), e.getCreateTime()));
        });
        return ResponseResult.success(animeFavVoList);
    }

    @PostMapping("/fav/add/{userId}/{animeId}")
    public ResponseResult<Boolean> addAnimeFav(@PathVariable("userId") Long userId, @PathVariable("animeId") Long animeId) {
        return userFeign.addAnimeFav(userId, animeId);
    }

    @PostMapping("/fav/del/{userId}/{animeId}")
    public ResponseResult<Boolean> delAnimeFav(@PathVariable("userId") Long userId, @PathVariable("animeId") Long animeId) {
        return userFeign.delAnimeFav(userId, animeId);
    }

    @GetMapping("/history/get/{userId}")
    public ResponseResult<List<AnimeHistoryVo>> getAnimeHistory(@PathVariable("userId") Long userId) {
        ResponseResult<List<AnimeHistoryDto>> data = userFeign.getAnimeHistory(userId);
        if (data.getCode()!= HttpCode.SUCCESS.getCode()) {
            return ResponseResult.failure(data.getMessage());
        }
        List<AnimeHistoryDto> animeHistoryDtoList = data.getData();
        List<AnimeHistoryVo> animeHistoryVoList  = new ArrayList<>();
        animeHistoryDtoList.forEach(e -> {
            BaseAnimeDto baseAnimeDto = animeFeign.getBaseAnime(e.getAnimeId()).getData();
            animeHistoryVoList.add(new AnimeHistoryVo(e.getAnimeId().toString(), baseAnimeDto.getTitle(),
                    baseAnimeDto.getCover(), e.getVisitedTime()));
        });
        return ResponseResult.success(animeHistoryVoList);
    }

    @PostMapping("/history/add/{userId}/{animeId}")
    public ResponseResult<Boolean> addAnimeHistory(@PathVariable("userId") Long userId,
                                                   @PathVariable("animeId") Long animeId) {
        return userFeign.addAnimeHistory(userId, animeId);
    }

    @PostMapping("/history/del/{userId}/{animeId}")
    public ResponseResult<Boolean> delAnimeHistory(@PathVariable("userId") Long userId,
                                                   @PathVariable("animeId") Long animeId) {
        return userFeign.delAnimeHistory(userId, animeId);
    }

    @PostMapping("/history/clear/{userId}")
    public ResponseResult<Boolean> clearAnimeHistory(@PathVariable("userId") Long userId) {
        return userFeign.clearAnimeHistory(userId);
    }

    @PostMapping("/progress/add/{userId}/{episodeId}/{progress}")
    public ResponseResult<Boolean> addEpisodeProgress(@PathVariable("userId") Long userId,
                                               @PathVariable("episodeId") Long episodeId,
                                               @PathVariable("progress") Double progress) {
        return userFeign.addEpisodeProgress(userId, episodeId, progress);
    }

    @GetMapping("/progress/get/{userId}")
    ResponseResult<List<PlayProgressVo>> getEpisodeProgress(@PathVariable("userId") Long userId) {
        ResponseResult<List<PlayProgressDto>> data = userFeign.getEpisodeProgress(userId);
        if (data.getCode()!= HttpCode.SUCCESS.getCode()) {
            return ResponseResult.failure(data.getMessage());
        }
        List<PlayProgressDto> playProgressDtoList = data.getData();
        List<PlayProgressVo> playProgressVoList = new ArrayList<>();
        playProgressDtoList.forEach(e -> {
            EpisodeDto episodeDto = animeFeign.getEpisode(e.getEpisodeId()).getData();
            if (episodeDto == null) {
                log.warn("分集未找到，分集id为"+e.getEpisodeId());
                return;
            }
            playProgressVoList.add(new PlayProgressVo(episodeDto.getAnimeId().toString(), "bilibili",
                    episodeDto.getTitle(), e.getProgress(), e.getEpisodeId().toString(), e.getCreateTime()));
        });
        return ResponseResult.success(playProgressVoList);
    }

    @GetMapping("/complement/{keyword}")
        public ResponseResult<List<String>> complement(@PathVariable("keyword") String keyword) {
        List<String> complementList = searchFeign.complement(keyword).getData();
        return ResponseResult.success(complementList);
    }
}
