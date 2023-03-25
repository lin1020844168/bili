package com.lin.bili.chat.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-11-01 15:41:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Message implements Serializable {
	private static final long serialVersionUID = 1L;


	protected Long id;
	/**
	 * 消息接收方
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	protected Long toUserId;
	/**
	 * 消息接收方
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	protected Long fromUserId;
	/**
	 * 发送方式
	 */
	protected Integer method;
	/**
	 * 消息类型
	 */
	protected Integer type;
	/**
	 * 发送时间
	 */
	protected Date sendTime;

	/**
	 * 与前端交流的令牌 可以快速帮前端定位消息位置,不用存数据库
	 */
	protected String token;
}
