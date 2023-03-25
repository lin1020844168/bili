package com.lin.bili.anime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.anime.dto.*;
import com.lin.bili.anime.po.Anime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnimeMapper extends BaseMapper<Anime> {
    @Select("SELECT * FROM anime WHERE id=#{id}")
    Anime getById(@Param("id") Long id);

    @Select("select id, cover, title, season from anime where id = #{id}")
    BaseAnimeDto getAnimeBaseById(@Param("id") Long id);

    DetailAnimeDto getAnimeDetailById(@Param("id") Long id);

    @Select("select * from anime")
    List<Anime> selectAll();

    @Select("select  title from anime")
    List<String> listTitle();

    
    List<ToEsAnimeDto> listToEsAnimeDto();

}
