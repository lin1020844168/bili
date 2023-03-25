package com.lin.bili.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeHistoryDto implements Serializable {
    /**
     * 番剧id
     */
    private Long animeId;
    /**
     * 创建时间
     */
    private Date visitedTime;
}
