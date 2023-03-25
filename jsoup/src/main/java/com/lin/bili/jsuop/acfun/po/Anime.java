package com.lin.bili.jsuop.acfun.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-10-23 17:47:09
 */
@Data
@TableName("anime_info")
public class Anime implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 番剧id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 简介
	 */
	private String introduction;
	/**
	 * 总集数
	 */
	private Integer total;
	/**
	 * 投币数
	 */
	private Integer coinCnt;
	/**
	 * 点赞数
	 */
	private Integer goodCnt;
	/**
	 * 总播放量
	 */
	private Integer playCnt;
	/**
	 * 番剧名
	 */
	private String name;
	/**
	 * 开播日期
	 */
	private Date begin;
	/**
	 * 封面图
	 */
	private String img;
	/**
	 * 收藏数
	 */
	private Integer collectionCnt;
	/**
	 * 开播状态 0连载中 1已完结 2即将上映
	 */
	private Integer state;


	/**
	 * gmt_create
	 */
	private Date gmtCreate;
	/**
	 * gmt_modified
	 */
	private Date gmtModified;

}
