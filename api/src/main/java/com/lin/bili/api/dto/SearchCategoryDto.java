package com.lin.bili.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCategoryDto implements Serializable {
    /**
     * 分类名
     */
    private String name;
    /**
     * 分类值
     */
    private String value;

    /**
     * 子分类
     */
    private List<SearchCategoryDto> children;
}
