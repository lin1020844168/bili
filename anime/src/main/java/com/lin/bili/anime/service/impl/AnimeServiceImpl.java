package com.lin.bili.anime.service.impl;

import com.lin.bili.anime.dto.BaseAnimeDto;
import com.lin.bili.anime.dto.DetailAnimeDto;
import com.lin.bili.anime.dto.ToEsAnimeDto;
import com.lin.bili.anime.mapper.AnimeMapper;
import com.lin.bili.anime.po.Anime;
import com.lin.bili.anime.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeServiceImpl implements AnimeService {
    @Autowired
    private AnimeMapper animeMapper;

    @Override
    public void add(Anime anime) {
        animeMapper.insert(anime);
    }

    @Override
    public Anime getAnime(Long id) {
        return animeMapper.getById(id);
    }

    @Override
    public BaseAnimeDto getBaseAnime(Long id) {
        return animeMapper.getAnimeBaseById(id);
    }

    @Override
    public DetailAnimeDto getDetailAnime(Long id) {
        DetailAnimeDto detailAnimeDto = animeMapper.getAnimeDetailById(id);
        return detailAnimeDto;
    }

    @Override
    public List<String> allTitle() {
        return animeMapper.listTitle();
    }

    @Override
    public List<ToEsAnimeDto> AllToEsAnime() {
        return animeMapper.listToEsAnimeDto();
    }
}
