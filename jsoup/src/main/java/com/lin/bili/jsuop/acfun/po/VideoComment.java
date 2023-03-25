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
@TableName("video_comment")
public class VideoComment implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 评论id
	 */
	@TableId
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 视频id
	 */
	private Long videoAnimeSubId;
	/**
	 * 点赞数
	 */
	private Integer goodCnt;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * gmt_modified
	 */
	private Date gmtModified;
	/**
	 * gmt_create
	 */
	private Date gmtCreate;

}
