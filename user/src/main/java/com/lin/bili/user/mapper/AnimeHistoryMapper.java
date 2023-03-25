package com.lin.bili.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.user.dto.AnimeHistoryDto;
import com.lin.bili.user.po.AnimeHistory;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
@Mapper
public interface AnimeHistoryMapper extends BaseMapper<AnimeHistory> {
    @Select("select anime_id, visited_time from anime_history_r where user_id = #{userId}")
    List<AnimeHistoryDto> listByUserId(@Param("userId") Long userId);

    @Delete("delete from anime_history_r where user_id = #{userId} and anime_id = #{animeId}")
    void deleteByUserIdAndAnimeId(@Param("userId") Long userId, @Param("animeId") Long animeId);

    @Select("select * from anime_history_r where user_id = #{userId} and anime_id = #{animeId}")
    AnimeHistory getByUserIdAndAnimeId(@Param("userId") Long userId, @Param("animeId") Long animeId);

    @Delete("delete from anime_history_r where user_id = #{userId}")
    void clearAll(@Param("userId") Long userId);

    @Update("update anime_history_r set visited_time = #{visitedTime} where user_id = #{userId} and anime_id = #{animeId}")
    void updateVisitedTimeByUserIdAndAnimeId(@Param("userId") Long userId, @Param("animeId") Long animeId, @Param("visitedTime") Date visitedTime);

}
