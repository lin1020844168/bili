package com.lin.bili.user.service;

import com.lin.bili.user.dto.AnimeFavDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AnimeFavService {
    void delAnimeFav(Long userId, Long animeId);

    List<AnimeFavDto> getAnimeFav(Long userId);

    void addAnimeFav(Long userId, Long animeId);
}
