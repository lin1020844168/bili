package com.lin.bili.anime.service;

import com.lin.bili.anime.dto.SearchCategoryDto;

import java.util.List;

public interface CategoryService {
    List<SearchCategoryDto> getCategory();

    void addCategory(int id, String name, String value, int parentId);
}
