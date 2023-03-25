package com.lin.bili.anime.po;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lin.bili.anime.AnimeApplication;
import com.lin.bili.anime.mapper.AnimeStyleMapper;
import com.lin.bili.anime.mapper.StyleMapper;
import com.lin.bili.anime.po.download.AbstractDownLoad;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class AnimeStyleTest {
    @Autowired
    private StyleMapper styleMapper;

    @Autowired
    private AnimeStyleMapper animeStyleMapper;

    @Test
    public void test() {
        AbstractDownLoad styleDownLoad = new AbstractDownLoad() {
            @Override
            public void save(JSONObject data) {
                JSONArray animeArray = data.get("data", JSONObject.class).get("list", JSONArray.class);
                animeArray.forEach(e -> {
                    JSONObject animeData = (JSONObject) e;
                    Long sid = animeData.get("season_id", Long.class);
                    String mid = animeData.get("media_id", String.class);
                    String json = getJsonById(mid);
                    JSONObject animeDetail = JSONUtil.parseObj(json).get("mediaInfo", JSONObject.class);
                    animeDetail.get("styles", JSONArray.class).forEach(o -> {
                        JSONObject style = (JSONObject) o;
                        Integer id = style.get("id", Integer.class);
                        String name = style.get("name", String.class);
                        synchronized (this) {
                            if (styleMapper.selectById(id)==null) {
                                styleMapper.insert(new Style(id, name));
                            }
                        }
                        animeStyleMapper.insert(new AnimeStyle(sid, id));
                    });
                });
            }
        };
        styleDownLoad.download();
    }
}