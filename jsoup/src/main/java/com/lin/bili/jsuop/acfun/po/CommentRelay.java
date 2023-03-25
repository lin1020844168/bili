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
@TableName("comment_relay")
public class CommentRelay implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 回复评价id
	 */
	@TableId
	private Long id;
	/**
	 * 评论id
	 */
	private Long commentId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 回复用户id
	 */
	private Long repayUserId;
	/**
	 * 点赞数
	 */
	private Integer goodCnt;
	/**
	 * gmt_create
	 */
	private Date gmtCreate;
	/**
	 * gmt_modified
	 */
	private Date gmtModified;

}
