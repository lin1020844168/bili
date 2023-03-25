package com.lin.bili.anime.po;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-12-21 23:19:48
 */
@Data
@TableName("anime_actor_r")
@AllArgsConstructor
@NoArgsConstructor
public class AnimeActor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 番剧id
     */
    @TableId
    private Long animeId;
    /**
     * 声优id
     */
    private Integer actorId;

}
