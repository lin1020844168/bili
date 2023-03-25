package com.lin.bili.anime.service.impl;

import com.lin.bili.anime.mapper.RegionMapper;
import com.lin.bili.anime.po.Region;
import com.lin.bili.anime.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl implements RegionService {
    @Autowired
    private RegionMapper regionMapper;

    @Override
    public void addRegion(String name) {
        regionMapper.insert(new Region(null, name));
    }
}
