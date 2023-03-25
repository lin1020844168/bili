package com.lin.bili.jsuop.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeDetailVo implements Serializable {
    /** 声优名字列表 */
    private String[] actors;
    /** 分类列表 */
    private String[] categories;
    /** 封面 */
    private String cover;
    /**首发时间 */
    private String first_date;
    /** 语言 */
    private String lang;
    /** 作者 */
    private String master;
    /** 播放列表 */
    private Map<String, List<Play>> playlist;
    /** 分数 */
    private String rank;
    /** 发布国家 */
    private String region;
    /** 动漫状态（更新、完结...） */
    private String season;
    /** 动漫名称 */
    private String title;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Play implements Serializable {
        private String link;
        private String title;
    }
}
