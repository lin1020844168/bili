package com.lin.bili.user.service.impl;

import com.lin.bili.user.dto.*;
import com.lin.bili.user.mapper.PlayProgressMapper;
import com.lin.bili.user.po.PlayProgress;
import com.lin.bili.user.service.AnimeProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AnimeProgressServiceImpl implements AnimeProgressService {
    @Autowired
    private PlayProgressMapper playProgressMapper;

    @Override
    public void addEpisodeProgress(Long userId, Long episodeId, Double progress) {
        if (existEpisodeProgress(userId, episodeId)) {
            playProgressMapper.updateCreateTimeAndProgressByUserIdAndEpisodeId(
                    userId, episodeId, progress, new Date());
        } else {
            playProgressMapper.insert(new PlayProgress(userId, episodeId, progress, new Date()));
        }
    }

    @Override
    public List<PlayProgressDto> getEpisodeProgress(Long userId) {
        return playProgressMapper.listPlayProgressDtoByUserId(userId);

    }

    private boolean existEpisodeProgress(Long userId, Long episodeId) {
        return playProgressMapper.getByUserIdAndEpisodeId(userId , episodeId) != null;
    }
}
