package com.lin.bili.anime.po;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lin.bili.anime.AnimeApplication;
import com.lin.bili.anime.mapper.ActorMapper;
import com.lin.bili.anime.mapper.AnimeActorMapper;
import com.lin.bili.anime.po.download.AbstractDownLoad;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class AnimeActorTest {
    @Autowired
    private AnimeActorMapper animeActorMapper;

    @Autowired
    private ActorMapper actorMapper;

    @Test
    public void test() {
        AbstractDownLoad actorMapperDownLoad = new AbstractDownLoad() {
            @Override
            protected void save(JSONObject data) {
                JSONArray animeArray = data.get("data", JSONObject.class).get("list", JSONArray.class);
                animeArray.forEach(e -> {
                    JSONObject animeData = (JSONObject) e;
                    Long sid = animeData.get("season_id", Long.class);
                    String mid = animeData.get("media_id", String.class);
                    String json = getJsonById(mid);
                    JSONObject animeDetail = JSONUtil.parseObj(json).get("mediaInfo", JSONObject.class);
                    Arrays.stream(animeDetail.get("actors", String.class).split("\\n"))
                            .forEach((actorName -> {
                                actorName = parseActor(actorName);
                                Integer actorId;
                                synchronized (this) {
                                    actorId = actorMapper.getIdByName(actorName);
                                    if (actorId == null) {
                                        actorMapper.insert(new Actor(null, actorName));
                                        actorId = actorMapper.getIdByName(actorName);
                                    }
                                }
                                try {
                                    animeActorMapper.insert(new AnimeActor(sid, actorId));
                                } catch (Exception exception) {
                                    System.out.println("主键重复了--");
                                }
                            }));

                });
            }
        };
        actorMapperDownLoad.download();
    }

    private String parseActor(String s) {
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i)==':' || s.charAt(i)=='：') {
                return s.substring(i+1);
            }
        }
        return s;
    }
}