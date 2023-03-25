package com.lin.bili.anime.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.IDLType;

/**
 *
 *
 * @author lin
 * @email 1020844168@qq.com
 * @date 2022-12-18 14:26:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("search_category")
public class SearchCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类值
     */
    private String value;
    /**
     * 父分类id
     */
    private Integer parentId;

}

