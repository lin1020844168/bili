package com.lin.bili.anime.dto;

import com.lin.bili.anime.po.Style;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailAnimeDto implements Serializable {
    /**
     * 风格列表
     */
    private List<String> styleList;
    /**
     *
     */
    private List<String> actorList;
    /**
     * 封面
     */
    private String cover;
    /**
     * 名称/标题
     */
    private String title;
    /**
     * 首发时间
     */
    private Date pubTime;
    /**
     * 发布国家
     */
    private String region;
    /**
     * 作者
     */
    private String author;
    /**
     * 当前状态
     */
    private String season;
    /**
     * 评分
     */
    private Integer rank;
}
