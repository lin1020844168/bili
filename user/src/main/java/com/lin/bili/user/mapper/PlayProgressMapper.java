package com.lin.bili.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.user.dto.PlayProgressDto;
import com.lin.bili.user.po.PlayProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface PlayProgressMapper extends BaseMapper<PlayProgress> {
    @Select("select * from play_progress where user_id = ${userId} and episode_id = #{episodeId}")
    PlayProgress getByUserIdAndEpisodeId(@Param(("userId")) Long userId, @Param("episodeId") Long episodeId);

    @Select("select episode_id,progress,create_time from play_progress where user_id = ${userId}")
    List<PlayProgressDto> listPlayProgressDtoByUserId(@Param(("userId")) Long userId);

    @Update("update play_progress set create_time = #{createTime}, progress = #{progress}  where user_id = ${userId} and episode_id = #{episodeId}")
    void updateCreateTimeAndProgressByUserIdAndEpisodeId(@Param(("userId")) Long userId,
                                                         @Param("episodeId") Long episodeId,
                                                         @Param("progress") Double progress,
                                                         @Param("createTime") Date createTime);
}
