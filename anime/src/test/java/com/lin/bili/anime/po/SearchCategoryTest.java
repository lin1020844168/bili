package com.lin.bili.anime.po;

import com.lin.bili.anime.AnimeApplication;
import com.lin.bili.anime.dto.SearchCategoryDto;
import com.lin.bili.anime.feign.JsoupFeign;
import com.lin.bili.anime.service.CategoryService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class SearchCategoryTest{
    @Autowired
    CategoryService categoryService;

    @Autowired
    JsoupFeign jsoupFeign;

    @org.junit.Test
    public void test() {
        int cnt = 1;
        categoryService.addCategory(cnt++, "番剧", "anime", 0);
        List<SearchCategoryDto> data = jsoupFeign.getCategoryConfig().getData().get(0).getChildren();
        for (SearchCategoryDto searchCategoryDto : data) {
            String curName = searchCategoryDto.getName();
            if (curName.equals("版权") || curName.equals("付费")) {
                continue;
            }
            int cur = cnt++;
            categoryService.addCategory(cur, curName, searchCategoryDto.getValue(), 1);
            List<SearchCategoryDto> children = searchCategoryDto.getChildren();
            for (SearchCategoryDto child : children) {
                categoryService.addCategory(cnt++, child.getName(), child.getValue(), cur);
            }
        }
    }
}
