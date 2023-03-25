package com.lin.bili.user.feign;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.user.dto.BaseAnimeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "anime", path = "/anime")
public interface AnimeFeign {
    @GetMapping("/get/base/{id}")
    ResponseResult<BaseAnimeDto> getBaseAnime(@PathVariable("id") Long id);

}
