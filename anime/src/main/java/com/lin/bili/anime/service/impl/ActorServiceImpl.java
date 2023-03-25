package com.lin.bili.anime.service.impl;

import com.lin.bili.anime.mapper.ActorMapper;
import com.lin.bili.anime.po.Actor;
import com.lin.bili.anime.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActorServiceImpl implements ActorService {
    @Autowired
    private ActorMapper actorMapper;

    @Override
    public void addActor(String name) {
        actorMapper.insert(new Actor(null, name));
    }
}
