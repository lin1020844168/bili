package com.lin.bili.anime.po;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-12-22 02:32:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("anime_style_r")
public class AnimeStyle implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 番剧id
     */
    @TableId
    private Long animeId;
    /**
     * 风格id
     */
    private Integer styleId;

}

