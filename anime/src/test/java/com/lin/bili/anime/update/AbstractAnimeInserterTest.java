package com.lin.bili.anime.update;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lin.bili.anime.AnimeApplication;
import com.lin.bili.anime.mapper.AnimeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class AbstractAnimeInserterTest {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AnimeMapper animeMapper;


    @Test
    public void test() {
        AbstractAnimeInserter inserter = new AbstractAnimeInserter(animeMapper, restTemplate) {
            @Override
            protected void doInsert(JSONObject data, Long animeId) {
                JSONObject result = data.get("result", JSONObject.class);
                Boolean isStarted = result.get("publish", JSONObject.class).get("is_started", Boolean.class);
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", animeId);
                updateWrapper.set("is_started", isStarted);
                animeMapper.update(null, updateWrapper);
            }
        };
        inserter.insert();

    }
}