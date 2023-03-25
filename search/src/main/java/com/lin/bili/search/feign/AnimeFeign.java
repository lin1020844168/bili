package com.lin.bili.search.feign;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.search.dto.ToEsAnimeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "anime", path = "/anime")
public interface AnimeFeign {
    @GetMapping("/all/toEs")
    ResponseResult<List<ToEsAnimeDto>> AllToEsAnime();

    @GetMapping("/all/title")
    ResponseResult<List<String>> allTitle();
}
