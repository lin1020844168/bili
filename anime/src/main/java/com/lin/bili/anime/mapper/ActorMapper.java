package com.lin.bili.anime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.anime.po.Actor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface ActorMapper extends BaseMapper<Actor> {
    @Select("select id from actor where name = #{name}")
    Integer getIdByName(@Param("name") String name);
}
