package com.lin.bili.video.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-10-23 17:47:09
 */
@Data
@TableName("video_info")
public class Video implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 视频id
	 */
	@TableId
	private Long id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 分区id
	 */
	private Integer partitionId;
	/**
	 * gmt_create
	 */
	private Date gmtCreate;
	/**
	 * gmt_modified
	 */
	private Date gmtModified;

}
