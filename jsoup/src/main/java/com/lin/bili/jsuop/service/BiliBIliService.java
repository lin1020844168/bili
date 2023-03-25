package com.lin.bili.jsuop.service;

import com.lin.bili.jsuop.dto.*;
import com.lin.bili.jsuop.vo.AnimeDetailVo;
import com.lin.bili.jsuop.vo.FilterVo;
import com.lin.bili.jsuop.vo.IndexVo;
import com.lin.bili.jsuop.vo.SearchVo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BiliBIliService {
    IndexVo getIndex() throws IOException;

    SearchVo search(String name, int page) throws IOException;

    ConfigDto getConfig() throws IOException;

    FilterVo filter(Map<String, String> params);

    AnimeDetailVo getAnimeDetail(String id) throws IOException;

    EpisodeOrgDto getEpisodeOrg(String id);

    VideoAudioUrlDto getEpisodeUrl(String id, String quality);

    EpisodeDto getEpisode(String episodeId);

    List<SearchCategoryDto> getCategoryConfig() throws IOException;
}
