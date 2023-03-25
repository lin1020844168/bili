package com.lin.bili.anime.controller;

import com.lin.bili.anime.dto.*;
import com.lin.bili.anime.po.Anime;
import com.lin.bili.anime.service.AnimeService;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anime")
public class AnimeController {
    @Autowired
    private AnimeService animeService;

    @PostMapping("/add")
    public ResponseResult<Boolean> addAnime(Anime anime) {
        animeService.add(anime);
        return ResponseResult.success(true);
    }

    @GetMapping("/get/base/{id}")
    public ResponseResult<BaseAnimeDto> getBaseAnime(@PathVariable("id") Long id) {
        BaseAnimeDto baseAnimeDtoList = animeService.getBaseAnime(id);
        return ResponseResult.success(baseAnimeDtoList);
    }


    @GetMapping("/get/detail/{id}")
    public ResponseResult<DetailAnimeDto> getDetailAnime(@PathVariable("id") Long id) {
        DetailAnimeDto detailAnimeDto =  animeService.getDetailAnime(id);
        return ResponseResult.success(detailAnimeDto);
    }

    @GetMapping("/all/title")
    public ResponseResult<List<String>> allTitle() {
        List<String> titles = animeService.allTitle();
        return ResponseResult.success(titles);
    }

    @GetMapping("/all/toEs")
    public ResponseResult<List<ToEsAnimeDto>> AllToEsAnime() {
        List<ToEsAnimeDto> toEsAnimeDtoList = animeService.AllToEsAnime();
        return ResponseResult.success(toEsAnimeDtoList);
    }

}
