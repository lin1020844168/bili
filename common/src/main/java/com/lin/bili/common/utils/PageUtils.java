package com.lin.bili.common.utils;

import lombok.Data;

import java.util.List;

/**
 * 分页实体类
 * T：泛型
 */
@Data
public class PageUtils<T> {
    /* 1 页码；第几页
       2 每页几 条
       3 数据库总共多条【sql查询】
                4 算总页数【算】
                5 当前页的数据集合【sql查询】
        */
    private Integer pageIndex;
    private Integer pageSize;
    private Integer totalCount;//总共多条
    private Integer totalPage;//总页数
    private List<T> list;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount=totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage() {
        //计算总页码
        this.totalPage=(this.totalCount%this.pageSize==0)?
                this.totalCount/this.pageSize:
                this.totalCount/this.pageSize+1;

    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}


