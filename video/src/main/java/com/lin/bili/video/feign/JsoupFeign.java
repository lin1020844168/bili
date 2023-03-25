package com.lin.bili.video.feign;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.video.dto.EpisodeOrgDto;
import com.lin.bili.video.dto.VideoAudioUrlDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "jsoup", path = "/bilibili")
public interface JsoupFeign {
    @GetMapping("/getEpisodeOrg/{anime_id}")
    ResponseResult<EpisodeOrgDto> getEpisodeOrg(@PathVariable("anime_id") String animeId);

    @GetMapping("/getEpisodeUrl/{id}/{quality}")
    ResponseResult<VideoAudioUrlDto> getEpisodeUrl(@PathVariable("id") String id, @PathVariable("quality") String quality);
}
