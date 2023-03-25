package com.lin.bili.user.service;

import com.lin.bili.user.dto.AnimeHistoryDto;

import java.util.List;

public interface AnimeHistoryService {
    List<AnimeHistoryDto> getAnimeHistory(Long userId);

    void addAnimeHistory(Long userId, Long animeId);

    void delAnimeHistory(Long userId, Long animeId);

    void clearAnimeHistory(Long userId);
}
