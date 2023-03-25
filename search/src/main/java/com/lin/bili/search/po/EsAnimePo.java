package com.lin.bili.search.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsAnimePo implements Serializable {
    /** 封面 */
    private String cover;
    /** 动漫id */
    private Long id;
    /** 动漫状态（更新、完结...） */
    private String season;
    /** 动漫名称 */
    private String title;
    /**动漫分区 id*/
    private Integer partitionId;
    /**追番数 */
    private Long favCount;
    /**评分 */
    private Double rank;
    /** 播放数量 */
    private Long playCount;
    /** 开播时间 */
    private Date pubTime;
    /** 类型id */
    private Integer seasonVersionId;
    /**配音类型id*/
    private Integer spokenLanguageTypeId;
    /** 地区id */
    private Integer regionId;
    /** 完结状态*/
    private Boolean isFinish;
    /**风格列表*/
    private List<Integer> styleIdList;
    /**番剧季度*/
    private Integer seasonMonth;
}
