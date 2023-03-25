package com.lin.bili.anime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.anime.dto.EpisodeDto;
import com.lin.bili.anime.po.Episode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface EpisodeMapper extends BaseMapper<Episode> {
    @Select("select id, title, anime_id from episode where id = #{id}")
    EpisodeDto getEpisodeDtoById(@Param("id") Long id);

    @Select("select id, title, anime_id from episode where anime_id = #{animeId}")
    List<EpisodeDto> getEpisodeDtoByAnimeId(@Param("animeId") Long animeId);
}
