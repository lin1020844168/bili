package com.lin.bili.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeFavDto implements Serializable {
    /**
     * 番剧id
     */
    private Long animeId;
    /**
     * 创建时间
     */
    private Date createTime;
}
