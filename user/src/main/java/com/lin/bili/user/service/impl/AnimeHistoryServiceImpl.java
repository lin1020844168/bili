package com.lin.bili.user.service.impl;

import com.lin.bili.user.dto.AnimeHistoryDto;
import com.lin.bili.user.mapper.AnimeHistoryMapper;
import com.lin.bili.user.po.AnimeHistory;
import com.lin.bili.user.service.AnimeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AnimeHistoryServiceImpl implements AnimeHistoryService {
    @Autowired
    private AnimeHistoryMapper animeHistoryMapper;

    @Override
    public List<AnimeHistoryDto> getAnimeHistory(Long userId) {
        List<AnimeHistoryDto> animeHistoryDtoList = animeHistoryMapper.listByUserId(userId);
        animeHistoryDtoList.sort((a, b) -> b.getVisitedTime().compareTo(a.getVisitedTime()));
        return animeHistoryDtoList;
    }

    @Override
    public void addAnimeHistory(Long userId, Long animeId) {
        if (existHistory(userId, animeId)) {
            animeHistoryMapper.updateVisitedTimeByUserIdAndAnimeId(userId, animeId, new Date());
        } else {
            animeHistoryMapper.insert(new AnimeHistory(userId, animeId, new Date()));
        }

    }

    private boolean existHistory(Long userId, Long animeId) {
        AnimeHistory animeHistory = animeHistoryMapper.getByUserIdAndAnimeId(userId, animeId);
        return animeHistory!=null;
    }

    @Override
    public void delAnimeHistory(Long userId, Long animeId) {
        animeHistoryMapper.deleteByUserIdAndAnimeId(userId, animeId);
    }

    @Override
    public void clearAnimeHistory(Long userId) {
        animeHistoryMapper.clearAll(userId);
    }

}
