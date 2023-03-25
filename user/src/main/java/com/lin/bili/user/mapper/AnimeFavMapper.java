package com.lin.bili.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.user.dto.AnimeFavDto;
import com.lin.bili.user.po.AnimeFav;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnimeFavMapper extends BaseMapper<AnimeFav> {
    @Select("select anime_id, create_time from anime_fav_r where user_id = #{userId} order by create_time")
    List<AnimeFavDto> listByUserId(@Param("userId") Long userId);

    @Delete("delete from anime_fav_r where user_id = #{userId} and anime_id = #{animeId}")
    void deleteByUserIdAndAnimeId(@Param("userId") Long userId, @Param("animeId") Long animeId);


}
