package com.lin.bili.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseAnimeDto implements Serializable {
    /**
     * 番剧id
     */
    private Long id;
    /**
     * 封面
     */
    private String cover;
    /**
     * 名称/标题
     */
    private String title;
    /**
     * 当前状态
     */
    private String season;
}
