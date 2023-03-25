package com.lin.bili.user.po;

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
 * @date 2022-12-16 11:24:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("anime_history_r")
public class AnimeHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId
    private Long userId;
    /**
     * 动漫id
     */
    private Long animeId;
    /**
     * 浏览时间
     */
    private Date visitedTime;

}
