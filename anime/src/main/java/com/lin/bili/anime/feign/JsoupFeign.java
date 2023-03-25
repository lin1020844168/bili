package com.lin.bili.anime.feign;

import com.lin.bili.anime.dto.SearchCategoryDto;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "jsoup", path = "/bilibili")
public interface JsoupFeign {
    @GetMapping("/getCategoryConfig")
    ResponseResult<List<SearchCategoryDto>> getCategoryConfig();
}
