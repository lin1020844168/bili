package com.lin.bili.anime.controller;

import com.lin.bili.anime.dto.EpisodeDto;
import com.lin.bili.anime.po.Episode;
import com.lin.bili.anime.service.EpisodeService;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/anime/episode")
public class EpisodeController {
    @Autowired
    private EpisodeService episodeService;

    @GetMapping("/get/{id}")
    public ResponseResult<EpisodeDto> getEpisode(@PathVariable("id") Long id) {
        EpisodeDto episodeDto = episodeService.getEpisode(id);
        return ResponseResult.success(episodeDto);
    }

    @GetMapping("/all/{animeId}")
    public ResponseResult<List<EpisodeDto>> allEpisode(@PathVariable("animeId")Long animeId) {
        List<EpisodeDto> episodeDtoList = episodeService.allEpisode(animeId);
        return ResponseResult.success(episodeDtoList);
    }
}
