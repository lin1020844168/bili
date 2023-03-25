package com.lin.bili.user.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-12-14 16:22:25
 */
@Data
@TableName("anime_fav_r")
public class AnimeFav implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId
    private Long userId;
    /**
     * 番剧id
     */
    private Long animeId;
    /**
     * 创建时间
     */
    private Date createTime;

}
