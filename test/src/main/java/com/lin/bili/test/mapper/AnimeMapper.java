package com.lin.bili.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.bili.test.po.Anime;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnimeMapper extends BaseMapper<Anime> {
}
