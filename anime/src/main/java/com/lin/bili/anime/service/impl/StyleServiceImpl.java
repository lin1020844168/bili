package com.lin.bili.anime.service.impl;

import com.lin.bili.anime.mapper.StyleMapper;
import com.lin.bili.anime.po.Style;
import com.lin.bili.anime.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StyleServiceImpl implements StyleService {
    @Autowired
    private StyleMapper styleMapper;

    @Override
    public void addStyle(String name) {
        styleMapper.insert(new Style(null, name));
    }
}
