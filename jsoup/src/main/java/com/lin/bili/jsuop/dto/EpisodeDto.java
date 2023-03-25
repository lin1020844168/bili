package com.lin.bili.jsuop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeDto implements Serializable {
    /**
     * 番剧id
     */
    private String animeId;

    /**
     * 当前标题
     */
    private String cur;
}
