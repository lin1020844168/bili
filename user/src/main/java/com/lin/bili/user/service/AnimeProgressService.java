package com.lin.bili.user.service;

import com.lin.bili.user.dto.AnimeFavDto;
import com.lin.bili.user.dto.AnimeHistoryDto;
import com.lin.bili.user.dto.PlayProgressDto;

import java.util.List;

public interface AnimeProgressService {
    void addEpisodeProgress(Long userId, Long episodeId, Double progress);

    List<PlayProgressDto> getEpisodeProgress(Long userId);
}
