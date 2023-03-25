package com.lin.bili.anime.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-12-20 03:34:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("anime")
public class Anime implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 番剧id
     */
    @TableId
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
     * 简介
     */
    @TableField(value = "`description`")
    private String description;
    /**
     * 首发时间
     */
    private Date pubTime;
    /**
     * 发布国家
     */
    private Integer regionId;
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
    @TableField(value = "`rank`")
    private Integer rank;
    /**
     * 番剧类型id
     */
    private Integer seasonVersionId;
    /**
     * 配音类型id
     */
    private Integer spokenLanguageTypeId;
    /**
     * 完结状态
     */
    private Integer isFinish;
    /**
     * 分区id
     */
    private Integer partitionId;
    /**
     * 追番数
     */
    private Long favCount;
    /**
     * 播放数
     */
    private Long playCount;
    /**
     * 开播状态
     */
    private Integer isStarted;
}
