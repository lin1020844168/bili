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
 * @date 2022-12-17 17:27:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("play_progress")
public class PlayProgress implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId
    private Long userId;
    /**
     * 分集id
     */
    private Long episodeId;
    /**
     * 播放进度
     */
    private Double progress;
    /**
     * 播放时间
     */
    private Date createTime;

}

