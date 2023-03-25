package com.lin.bili.api.feign;

import com.lin.bili.api.dto.*;
import com.lin.bili.api.vo.AnimeDetailVo;
import com.lin.bili.api.vo.FilterVo;
import com.lin.bili.api.vo.IndexVo;
import com.lin.bili.api.vo.SearchVo;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "jsoup", path = "/bilibili")
public interface JsoupFeign {
    @GetMapping("/getIndex")
    ResponseResult<IndexVo> getIndex();

    @GetMapping("/getConfig")
    ResponseResult<ConfigDto> getConfig();

    @GetMapping("/search/{name}")
    ResponseResult<SearchVo> search(@PathVariable("name") String name, @RequestParam("page") int page);

    @GetMapping("/filter")
    ResponseResult<FilterVo> filter(@RequestParam Map<String, String> params);

    @GetMapping("/getAnimeDetail/{id}")
    ResponseResult<AnimeDetailVo> getAnimeDetail(@PathVariable("id") String id);

    @GetMapping("/getCategoryConfig")
    ResponseResult<List<SearchCategoryDto>> getCategoryConfig();
}
