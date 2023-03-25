package com.lin.bili.jsuop.acfun.po;

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
@TableName("anime_video_info")
public class AnimeVideo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 番剧id
	 */
	@TableId
	private Integer animeId;
	/**
	 * 当前集数
	 */
	private Integer cur;
	/**
	 * 视频id
	 */
	private Long videoId;
	/**
	 * 评论数
	 */
	private Integer commentCnt;
	/**
	 * gmt_modified
	 */
	private Date gmtModified;
	/**
	 * gmt_create
	 */
	private Date gmtCreate;

}
