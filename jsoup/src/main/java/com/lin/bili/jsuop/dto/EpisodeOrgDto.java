package com.lin.bili.jsuop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 请求的分集路径关键信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeOrgDto implements Serializable {
    /**
     * 源信息
     */
    private String key;
    /**
     * 源路径
     */
    private String src;
    /**
     * 画质列表
     */
    private List<Quality> qualities = new ArrayList<>();
    /**
     * 分集列表
     */
    private List<Episode> episodes = new ArrayList<>();

    /**
     * 画质
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Quality implements Serializable {
        /**
         * 画质值
         */
        private int value;
        /**
         * 画质描述
         */
        private String desc;
    }

    /**
     * 分集
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Episode implements Serializable {
        /**
         * 分集信息
         */
        private String id;
        /**
         * 当前集数
         */
        private String title;

        /**
         * 标题
         */
        private String longTitle;
    }
}
