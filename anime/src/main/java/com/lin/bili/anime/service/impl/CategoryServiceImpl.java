package com.lin.bili.anime.service.impl;

import com.lin.bili.anime.dto.SearchCategoryDto;
import com.lin.bili.anime.mapper.SearchCategoryMapper;
import com.lin.bili.anime.po.SearchCategory;
import com.lin.bili.anime.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private SearchCategoryMapper searchCategoryMapper;

    @Override
    public List<SearchCategoryDto> getCategory() {
        List<SearchCategory> categoryList = searchCategoryMapper.selectList(null);
        List<SearchCategoryDto> roots = new ArrayList<>();
        List<SearchCategoryDto> all = new ArrayList<>();
        Map<Integer, SearchCategoryDto> searchCategoryDtoMap = new HashMap<>();
        categoryList.forEach(e -> {
            SearchCategoryDto node =  new SearchCategoryDto(e.getName(), e.getValue(), new ArrayList<>());
            all.add(node);
            searchCategoryDtoMap.put(e.getId(), node);
            if (e.getParentId()==0) {
                roots.add(node);
            }
        });
        for (int i = 0; i < all.size(); i++) {
            SearchCategory searchCategory = categoryList.get(i);
            if (!searchCategoryDtoMap.containsKey(searchCategory.getParentId())) {
                continue;
            }
            SearchCategoryDto parent = searchCategoryDtoMap.get(searchCategory.getParentId());
            parent.getChildren().add(all.get(i));
        }
        return roots;
    }

    @Override
    public void addCategory(int id, String name, String value, int parentId) {
        searchCategoryMapper.insert(new SearchCategory(id, name, value, parentId));
    }
}
