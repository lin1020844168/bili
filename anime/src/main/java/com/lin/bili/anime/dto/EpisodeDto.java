package com.lin.bili.anime.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDto implements Serializable {
    /**
     * 当前集数
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 番剧id
     */
    private Long animeId;
}
