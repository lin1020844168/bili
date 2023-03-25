package com.lin.bili.jsuop.controller;

import com.lin.bili.common.utils.ParseBilibiliUtils;
import com.lin.bili.common.utils.ResponseResult;


import com.lin.bili.jsuop.dto.*;
import com.lin.bili.jsuop.service.BiliBIliService;
import com.lin.bili.jsuop.vo.AnimeDetailVo;
import com.lin.bili.jsuop.vo.FilterVo;
import com.lin.bili.jsuop.vo.IndexVo;
import com.lin.bili.jsuop.vo.SearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bilibili")
public class BiliBiliController {
    @Autowired
    private BiliBIliService biliBIliService;

    @GetMapping("/getConfig")
    public ResponseResult<ConfigDto> getConfig() throws IOException {
        ConfigDto data =  biliBIliService.getConfig();
        return ResponseResult.success(data);
    }

    @GetMapping("/getIndex")
    public ResponseResult<IndexVo> getIndex() throws IOException {
        IndexVo data = biliBIliService.getIndex();
        return ResponseResult.success(data);
    }

    @GetMapping("/search/{name}")
    public ResponseResult<SearchVo> search(@PathVariable("name") String name, @RequestParam("page") int page) throws IOException {
        SearchVo data = biliBIliService.search(name, page);
        return ResponseResult.success(data);
    }

    @GetMapping("/filter")
    public ResponseResult<FilterVo> filter(@RequestParam Map<String, String> params) {
        FilterVo data = biliBIliService.filter(params);
        return ResponseResult.success(data);
    }

    @GetMapping("/getAnimeDetail/{anime_id}")
    public ResponseResult<AnimeDetailVo> getAnimeDetail(@PathVariable("anime_id") String animeId) throws IOException {
        AnimeDetailVo data =  biliBIliService.getAnimeDetail(animeId);
        return ResponseResult.success(data);
    }

    @GetMapping("/getEpisodeOrg/{anime_id}")
    public ResponseResult<EpisodeOrgDto> getEpisodeOrg(@PathVariable("anime_id") String animeId) {
        EpisodeOrgDto episodeOrgs =  biliBIliService.getEpisodeOrg(animeId);
        return ResponseResult.success(episodeOrgs);
    }

    @GetMapping("/getEpisodeUrl/{id}/{quality}")
    public ResponseResult<VideoAudioUrlDto> getEpisodeUrl(@PathVariable("id") String id, @PathVariable("quality") String quality) {
        VideoAudioUrlDto videoAudioUrl = biliBIliService.getEpisodeUrl(id, quality);
        return ResponseResult.success(videoAudioUrl);
    }

    @GetMapping("/getEpisode/{episode_id}")
    public ResponseResult<EpisodeDto> getEpisode(@PathVariable("episode_id") String episodeId) {
        EpisodeDto episodeDto = biliBIliService.getEpisode(episodeId);
        return ResponseResult.success(episodeDto);
    }

    @GetMapping("/getCategoryConfig")
    public ResponseResult<List<SearchCategoryDto>> getCategoryConfig() throws IOException {
        List<SearchCategoryDto> searchCategoryDtoList = biliBIliService.getCategoryConfig();
        return ResponseResult.success(searchCategoryDtoList);
    }
}
