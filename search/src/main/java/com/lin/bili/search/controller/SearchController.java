package com.lin.bili.search.controller;

import com.lin.bili.common.utils.PageUtils;
import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.search.dto.BaseAnimeDto;
import com.lin.bili.search.exception.server.NotSuchStrategyException;
import com.lin.bili.search.exception.server.StrategyNotInitException;
import com.lin.bili.search.service.SearchService;
import com.lin.bili.search.po.EsAnimePo;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/complement/{keyword}")
    public ResponseResult<List<String>> complement(@PathVariable("keyword") String keyword) throws IOException {
        List<String> complementList = searchService.complement(keyword);
        return ResponseResult.success(complementList);
    }

    @GetMapping("/hot/{count}")
    public ResponseResult<List<String>> hot(@PathVariable("count") String count) {
        List<String> hot = searchService.hot(Integer.parseInt(count));
        return ResponseResult.success(hot);
    }

    @GetMapping("/anime/{keyword}/{pageCnt}/{pageSize}")
    public ResponseResult<PageUtils<BaseAnimeDto>> listAnimeByKeyword(@PathVariable("keyword") String keyword,
                                                                      @PathVariable("pageCnt") Integer pageCnt,
                                                                      @PathVariable("pageSize") Integer pageSize) throws IOException {
        PageUtils<BaseAnimeDto> esAnimePoList = searchService.listAnimeByKeyword(keyword, pageCnt, pageSize);
        return ResponseResult.success(esAnimePoList);
    }

    @PostMapping("/anime/add")
    public ResponseResult addAnime(EsAnimePo esAnimePo) throws IOException {
        searchService.addAnime(esAnimePo);
        return ResponseResult.success();
    }

    @PostMapping("/anime/bulk")
    public ResponseResult bulkAnime(List<EsAnimePo> esAnimePoList) throws IOException {
        searchService.bulkAddAnime(esAnimePoList);
        return ResponseResult.success();
    }

    // param:partition=1, page=1, order=3, season_version=-1, spoken_language_type=-1, area=-1, is_finish=-1, season_month=-1, year=-1, style_id=-1
    @GetMapping("/anime/filter/{pageCnt}/{pageSize}")
    public ResponseResult<PageUtils<BaseAnimeDto>> filterAnime(@RequestParam Map<String, String> params,
                                                               @PathVariable("pageCnt") Integer pageCnt,
                                                               @PathVariable("pageSize") Integer pageSize) throws NotSuchStrategyException, StrategyNotInitException, IOException, ParseException {
        PageUtils<BaseAnimeDto> pageUtils = searchService.filterAnime(params, pageCnt, pageSize);
        return ResponseResult.success(pageUtils);
    }
}
