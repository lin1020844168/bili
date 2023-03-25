package com.lin.bili.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPageDto implements Serializable{
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
