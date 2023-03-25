package com.lin.bili.anime.update;

import cn.hutool.json.JSONObject;
import com.lin.bili.anime.AnimeApplication;
import com.lin.bili.anime.mapper.AnimeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class AnimeUpdaterTest {
    @Autowired
    AnimeMapper animeMapper;

    @Autowired
    AnimeUpdater animeUpdater;

    @Test
    public void getJson() {
        String jsonById = new AbstractDownLoad() {
            @Override
            protected void save(JSONObject data) {

            }
        }.getJsonById("28339619");
        System.out.println(jsonById);
    }

    @Test
    public void test() {
        animeUpdater.update();
    }
}