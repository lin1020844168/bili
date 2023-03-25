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
 * @date 2022-12-24 23:37:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("episode")
public class Episode implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分集id
     */
    @TableId
    private Long id;
    /**
     * 当前集数
     */
    private Integer cur;
    /**
     * 标题
     */
    private String title;
    /**
     * 番剧id
     */
    private Long animeId;

}

