package com.lin.bili.test.vo;

import lombok.Data;

@Data
public class EsAnimeVo {
    /**
     * 总集数
     */
    private int total;

    /**
     * 番剧名
     */
    private String name;

    /**
     * 番剧id
     */
    private Integer id;

    /**
     * 播放数
     */
    private int playCnt;

    /**
     * 收藏数
     */
    private int collectionCnt;

    /**
     * 开播状态
     */
    private int state;

    /**
     * 封面地址
     */
    private String img;

    /**
     * 简介
     */
    private String introduction;

}
