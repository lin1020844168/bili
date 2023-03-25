package com.lin.bili.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.video.po.VideoUrl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VideoUrlMapper extends BaseMapper<VideoUrl> {
    @Select("select url from video_url where video_id = #{id} and quality = #{quality}")
    String getUrlByVideoIdAndQuality(@Param("id") long id, @Param("quality") int quality);

    @Insert("insert into video_url(video_id, quality, url) values(#{id}, #{quality}, #{url})")
    void insertVideoUrl(@Param("id") long id, @Param("quality") int quality, @Param("url") String url);
}
