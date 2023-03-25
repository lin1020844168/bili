package com.lin.bili.api.feign;

import com.lin.bili.api.dto.BaseAnimeDto;
import com.lin.bili.common.utils.PageUtils;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "search", path = "/search")
public interface SearchFeign {
    @GetMapping("/complement/{keyword}")
    ResponseResult<List<String>> complement(@PathVariable("keyword") String keyword);

    @GetMapping("/anime/{keyword}/{pageCnt}/{pageSize}")
    ResponseResult<PageUtils<BaseAnimeDto>> listAnimeByKeyword(@PathVariable("keyword") String keyword,
                                                                      @PathVariable("pageCnt") Integer pageCnt,
                                                                      @PathVariable("pageSize") Integer pageSize);
    @GetMapping("/anime/filter/{pageCnt}/{pageSize}")
    ResponseResult<PageUtils<BaseAnimeDto>> filterAnime(@RequestParam Map<String, String> params,
                                                        @PathVariable("pageCnt") Integer pageCnt,
                                                        @PathVariable("pageSize") Integer pageSize);
}
