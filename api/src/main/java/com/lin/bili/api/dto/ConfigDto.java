package com.lin.bili.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDto implements Serializable {
    private List<Type> filtersConfig;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Type implements Serializable {
        /** 分类-名称 */
        private String name;
        /** 分类-id */
        private int id;
        /** 分类-类型列表 */
        private List<Category> categories;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Category implements Serializable {
        /** 分类-名称 */
        private String classname;
        /** 分类-id */
        private int classid;
    }
}
