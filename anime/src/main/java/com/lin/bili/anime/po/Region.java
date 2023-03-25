package com.lin.bili.anime.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-12-20 01:23:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("region")
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 地区id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 地区名
     */
    private String name;

}
