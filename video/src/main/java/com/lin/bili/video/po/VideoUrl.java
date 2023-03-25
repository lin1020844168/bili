package com.lin.bili.video.po;

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
 * @date 2022-12-11 22:15:44
 */
@Data
@TableName("video_url")
public class VideoUrl implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 视频id
     */
    @TableId
    private Long videoId;
    /**
     * 视频路径
     */
    private String url;
    /**
     * 视频品质
     */
    private Integer quality;

}
