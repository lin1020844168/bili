package com.lin.bili.anime.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @date 2022-12-21 23:01:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("actor")
public class Actor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 声优id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 声优名字
     */
    private String name;

}
