package com.lin.bili.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayProgressVo implements Serializable {
    /** 动漫id */
    private String comicId;
    /** 播放源id */
    private String orgId;
    /** 播放级数 */
    private String name;
    /** 播放进度 */
    private Double progress;
    /** 分集id */
    private String episodeId;
    /** 创建时间 */
    private Date date;
}
