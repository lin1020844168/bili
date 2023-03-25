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
 * @date 2022-12-22 01:51:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("style")
public class Style implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 风格id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 风格名称
     */
    private String name;

}

