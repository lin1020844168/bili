package com.lin.bili.jsuop.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchVo implements Serializable {

    /** 当前页条数 */
    private int size;
    /** 总条数 */
    private int total;
    /** 动漫列表 */
    private List<Result> results;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result implements Serializable {

        /** 分类列表 */
        private String category;
        /** 封面 */
        private String cover;
        /** 首发时间 */
        private String date;
        /** 介绍 */
        private String description;
        /** 动漫id */
        private String id;
        /** 动漫状态（更新、完结...） */
        private String season;
        /** 动漫名称 */
        private String title;
    }
}
