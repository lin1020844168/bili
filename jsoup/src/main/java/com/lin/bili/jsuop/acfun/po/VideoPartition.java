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
@TableName("video_partition")
public class VideoPartition implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分区id
	 */
	@TableId
	private Integer id;
	/**
	 * 分区名
	 */
	private String name;
	/**
	 * 分区表名
	 */
	private String tableName;
	/**
	 * gmt_modified
	 */
	private Date gmtModified;
	/**
	 * gmt_create
	 */
	private Date gmtCreate;

}
