package com.lin.bili.video.feign;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.video.dto.EpisodeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "anime", path = "/anime")
public interface AnimeFeign {
    @GetMapping("/episode/all/{animeId}")
    ResponseResult<List<EpisodeDto>> allEpisode(@PathVariable("animeId")Long animeId);
}
