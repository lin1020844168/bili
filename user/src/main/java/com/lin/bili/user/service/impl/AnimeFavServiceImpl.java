package com.lin.bili.user.service.impl;

import com.lin.bili.user.dto.AnimeFavDto;
import com.lin.bili.user.mapper.AnimeFavMapper;
import com.lin.bili.user.po.AnimeFav;
import com.lin.bili.user.service.AnimeFavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AnimeFavServiceImpl implements AnimeFavService {
    @Autowired
    private AnimeFavMapper animeFavMapper;

    @Override
    public void addAnimeFav(Long userId, Long animeId) {
        AnimeFav animeFav = new AnimeFav();
        animeFav.setUserId(userId);
        animeFav.setAnimeId(animeId);
        animeFav.setCreateTime(new Date());
        animeFavMapper.insert(animeFav);

    }

    @Override
    public void delAnimeFav(Long userId, Long animeId) {
        animeFavMapper.deleteByUserIdAndAnimeId(userId, animeId);
    }

    @Override
    public List<AnimeFavDto> getAnimeFav(Long userId) {
        return animeFavMapper.listByUserId(userId);
    }
}
