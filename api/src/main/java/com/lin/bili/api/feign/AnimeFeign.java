package com.lin.bili.api.feign;

import com.lin.bili.api.dto.BaseAnimeDto;
import com.lin.bili.api.dto.DetailAnimeDto;
import com.lin.bili.api.dto.EpisodeDto;
import com.lin.bili.api.dto.SearchCategoryDto;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "anime", path = "/anime")
public interface AnimeFeign {
    @GetMapping("/category/get")
    ResponseResult<List<SearchCategoryDto>> getCategory();

    @GetMapping("/get/detail/{id}")
    ResponseResult<DetailAnimeDto> getAnimeDetail(@PathVariable("id") Long id);

    @GetMapping("/get/base/{id}")
    ResponseResult<BaseAnimeDto> getBaseAnime(@PathVariable("id") Long id);

    @GetMapping("/episode/get/{id}")
    ResponseResult<EpisodeDto> getEpisode(@PathVariable("id") Long id);
}
