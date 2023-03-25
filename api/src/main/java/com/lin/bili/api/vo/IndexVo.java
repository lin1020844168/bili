package com.lin.bili.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//首页数据
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexVo implements Serializable {
    private Map<String, List<Hot>> hots;
    private List<Banner> banner;
    private Map<String, List<PerweekComic>> perweek;
    private List<Latest> latest;
    private List<JapanComic> japancomic;
    private List<ChineseComic> chinese_comic;
    private List<TheatreComic> theatre_comic;

    /** 完结日漫 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JapanComic implements Serializable {
        /** 封面 */
        private String cover;

        /** 动漫id */
        private String id;

        /** 动漫状态（更新、完结...） */
        private String season;

        /** 动漫名称 */
        private String title;

    }

    /** 热门动漫 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Hot implements Serializable {
        /** 封面 */
        private String cover;

        /** 动漫id */
        private String id;

        /** 动漫状态（更新、完结...） */
        private String season;

        /** 动漫名称 */
        private String title;

        /** 首发时间 */
        private String date;

        /** 介绍 */
        private String description;

    }

    /** 轮播 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Banner implements Serializable {
        /** 封面 */
        private String cover;
        /** 动漫id */
        private String id;
        /** 动漫名称 */
        private String title;
    }

    /** 国漫 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChineseComic implements Serializable {
        /** 封面 */
        private String cover;
        /** 动漫id */
        private String id;
        /** 动漫状态（更新、完结...） */
        private String season;
        /** 动漫名称 */
        private String title;
    }

    /** 最新更新 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Latest implements Serializable {
        /** 封面 */
        private String cover;

        /** 动漫id */
        private String id;

        /** 动漫状态（更新、完结...） */
        private String season;

        /** 动漫名称 */
        private String title;

    }

    /** 周更新列表 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PerweekComic implements Serializable {
        /** 动漫id */
        private String id;
        /** 动漫状态（更新、完结...） */
        private String season;
        /** 动漫名称 */
        private String title;
    }

    /** 推荐动漫番外 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TheatreComic implements Serializable {
        /** 封面 */
        private String cover;
        /** 动漫id */
        private String id;
        /** 动漫状态（更新、完结...） */
        private String season;
        /** 动漫名称 */
        private String title;
    }
}
