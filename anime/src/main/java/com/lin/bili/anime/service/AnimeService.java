package com.lin.bili.anime.service;

import com.lin.bili.anime.dto.BaseAnimeDto;
import com.lin.bili.anime.dto.DetailAnimeDto;
import com.lin.bili.anime.dto.ToEsAnimeDto;
import com.lin.bili.anime.po.Anime;

import java.util.List;

public interface AnimeService {
    void add(Anime anime);

    Anime getAnime(Long id);

    BaseAnimeDto getBaseAnime(Long id);

    DetailAnimeDto getDetailAnime(Long id);

    List<String> allTitle();

    List<ToEsAnimeDto> AllToEsAnime();
}
