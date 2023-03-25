package com.lin.bili.anime.service.impl;

import com.lin.bili.anime.dto.EpisodeDto;
import com.lin.bili.anime.mapper.EpisodeMapper;
import com.lin.bili.anime.service.EpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpisodeServiceImpl implements EpisodeService {
    @Autowired
    private EpisodeMapper episodeMapper;

    @Override
    public EpisodeDto getEpisode(Long id) {
        return episodeMapper.getEpisodeDtoById(id);
    }

    @Override
    public List<EpisodeDto> allEpisode(Long animeId) {
        return episodeMapper.getEpisodeDtoByAnimeId(animeId);
    }
}
