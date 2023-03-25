package com.lin.bili.anime.controller;

import com.lin.bili.anime.dto.SearchCategoryDto;
import com.lin.bili.anime.service.CategoryService;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anime/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/get")
    public ResponseResult<List<SearchCategoryDto>> getCategory() {
        List<SearchCategoryDto> searchCategoryDtoList =  categoryService.getCategory();
        return ResponseResult.success(searchCategoryDtoList);
    }

    @PostMapping("/add/{id}/{name}/{value}/{parentId}")
    public ResponseResult<Boolean> addCategory(@PathVariable("id") int id,
                                               @PathVariable("name") String name,
                                               @PathVariable("value") String value,
                                               @PathVariable("parentId") int parentId) {
        categoryService.addCategory(id, name, value, parentId);
        return ResponseResult.success(true);
    }
}
