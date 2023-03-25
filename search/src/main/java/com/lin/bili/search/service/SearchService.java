package com.lin.bili.search.service;

import com.lin.bili.common.utils.PageUtils;
import com.lin.bili.search.dto.BaseAnimeDto;
import com.lin.bili.search.exception.server.NotSuchStrategyException;
import com.lin.bili.search.exception.server.StrategyNotInitException;
import com.lin.bili.search.po.EsAnimePo;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface SearchService {
    PageUtils<BaseAnimeDto> listAnimeByKeyword(String keyword, Integer pageCnt, Integer pageSize) throws IOException;

    List<String> hot(int count);

    void addAnime(EsAnimePo esAnimePo) throws IOException;

    void bulkAddAnime(List<EsAnimePo> esAnimePoList) throws IOException;

    void updateAnime(EsAnimePo esAnimePo) throws IOException;

    void bulkUpdateAnime(List<EsAnimePo> esAnimePoList) throws IOException;

    List<String> complement(String keyword) throws IOException;

    void addComplement(String keyword) throws IOException;

    void bulkAddComplement(List<String> keywordList) throws IOException;

    PageUtils<BaseAnimeDto> filterAnime(Map<String, String> params, int pageCnt, int pageSize) throws NotSuchStrategyException, StrategyNotInitException, IOException, ParseException;
}
