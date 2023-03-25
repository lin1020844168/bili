package com.lin.bili.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeFavVo implements Serializable {
    /** 动漫id */
    private String comicId;
    /** 动漫名称 */
    private String comicName;
    /** 动漫封面 */
    private String comicCover;
    /** 收藏时间 */
    private Date favDate;

}
