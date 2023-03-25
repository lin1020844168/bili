package com.lin.bili.anime.service;

import com.lin.bili.anime.dto.EpisodeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface EpisodeService {
    EpisodeDto getEpisode(Long id);
    List<EpisodeDto> allEpisode(Long animeId);
}
