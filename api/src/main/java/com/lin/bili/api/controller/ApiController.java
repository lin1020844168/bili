package com.lin.bili.api.controller;

import com.lin.bili.api.dto.*;
import com.lin.bili.api.feign.AnimeFeign;
import com.lin.bili.api.feign.SearchFeign;
import com.lin.bili.api.paramMapper.FilterParamMapper;
import com.lin.bili.api.vo.*;
import com.lin.bili.common.utils.PageUtils;
import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.api.feign.JsoupFeign;
import com.lin.bili.api.feign.VideoFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private JsoupFeign jsoupFeign;

    @Autowired
    private VideoFeign videoFeign;

    @Autowired
    private AnimeFeign animeFeign;

    @Autowired
    private SearchFeign searchFeign;


    private ThreadLocal<DateFormat> dateFormatThreadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    @GetMapping("/getIndex")
    public ResponseResult<IndexVo> getIndex() throws IOException {
        ResponseResult<IndexVo> resp = jsoupFeign.getIndex();
        return resp;
    }

//    @GetMapping("/getConfig")
//    public ResponseResult<ConfigDto> getConfig() throws IOException {
//        ResponseResult<ConfigDto> resp = jsoupFeign.getConfig();
//        return resp;
//    }

    @GetMapping("/search/{keyword}")
    public ResponseResult<SearchVo> search(@PathVariable("keyword") String keyword, @RequestParam("page") int page) throws IOException {
//        ResponseResult<SearchVo> resp = jsoupFeign.search(keyword, page);
        PageUtils<BaseAnimeDto> data = searchFeign.listAnimeByKeyword(keyword, page, 12).getData();
        SearchVo searchVo = new SearchVo();
        searchVo.setSize(data.getPageSize());
        searchVo.setTotal(data.getTotalCount());
        List<SearchVo.Result> resultList = data.getList().stream().map(e -> new SearchVo.Result(null, e.getCover(), null,
                null, e.getId().toString(), e.getSeason(), e.getTitle())).collect(Collectors.toList());
        searchVo.setResults(resultList);
        return ResponseResult.success(searchVo);
    }

    // param:partition=1, page=1, order=3, season_version=-1, spoken_language_type=-1, area=-1, is_finish=-1,
    // season_month=-1, year=-1, style_id=-1
    @GetMapping("/filter")
    public ResponseResult<FilterVo> filter(@RequestParam Map<String, String> params) {
        FilterParamMapper filterParamMapper = new FilterParamMapper();
        Map<String, String> mapper = filterParamMapper.mapper(params);
//        ResponseResult<FilterVo> resp = jsoupFeign.filter(params);
        int page = Integer.parseInt(params.get("page"));
        PageUtils<BaseAnimeDto> data = searchFeign.filterAnime(mapper, page, 30).getData();
        List<FilterVo.Result> resultList = data.getList().stream().map(e -> new FilterVo.Result(e.getCover(),
                e.getId().toString(), e.getSeason(), e.getTitle())).collect(Collectors.toList());
        FilterVo filterVo = new FilterVo();
        filterVo.setSize(data.getPageSize());
        filterVo.setTotal(data.getTotalCount());
        filterVo.setResults(resultList);
        return ResponseResult.success(filterVo);
    }

    @GetMapping("/getAnimeDetail/{id}")
    public ResponseResult<AnimeDetailVo> getAnimeDetail(@PathVariable("id") Long id) throws IOException {
//        ResponseResult<AnimeDetailVo> resp = jsoupFeign.getAnimeDetail(id);
        DetailAnimeDto detailAnimeDto = animeFeign.getAnimeDetail(id).getData();
        String[] actors = detailAnimeDto.getActorList().toArray(new String[0]);
        String[] categories = detailAnimeDto.getStyleList().toArray(new String[0]);
        String first_date = dateFormatThreadLocal.get().format(detailAnimeDto.getPubTime());
        String rank = detailAnimeDto.getRank()/10.0+"";
        AnimeDetailVo animeDetailVo = new AnimeDetailVo(actors, categories, detailAnimeDto.getCover(),
                first_date, "", detailAnimeDto.getAuthor(), null, rank, detailAnimeDto.getRegion(),
                detailAnimeDto.getSeason(), detailAnimeDto.getTitle());
        return ResponseResult.success(animeDetailVo);
    }

    @GetMapping("/getEpisodeOrg/{id}")
    public ResponseResult<List<EpisodeOrgDto>> getEpisodeOrg(@PathVariable("id") String id) {
        ResponseResult<List<EpisodeOrgDto>> resp = videoFeign.getEpisodeOrg(id);
        return resp;
    }

    @GetMapping("/getCategoryConfig")
    public ResponseResult<List<SearchCategoryDto>> getCategoryConfig() {
//        ResponseResult<List<SearchCategoryDto>> resp = jsoupFeign.getCategoryConfig();
        ResponseResult<List<SearchCategoryDto>> category = animeFeign.getCategory();
        return category;
    }
}
