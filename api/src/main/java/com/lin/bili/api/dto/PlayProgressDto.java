package com.lin.bili.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayProgressDto implements Serializable {
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
