package com.lin.bili.anime.po;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.bili.anime.AnimeApplication;
import com.lin.bili.anime.mapper.AnimeMapper;
import com.lin.bili.anime.mapper.EpisodeMapper;
import com.lin.bili.anime.po.download.AbstractDownLoad;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class EpisodeTest {
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private EpisodeMapper episodeMapper;

    @Autowired
    private AnimeMapper animeMapper;

    @Test
    public void test0() {
        List<Anime> anime = animeMapper.selectAll();
        anime.forEach(e -> {
            Long sid = e.getId();
            List<Episode> ep = episodeMapper.selectList(new QueryWrapper<Episode>().eq("anime_id", sid));
            if (ep!=null && ep.size()>0) {
                return;
            }
            System.out.println(sid);
            try {
                JSONObject epData = restTemplate.getForObject("https://api.bilibili.com/pgc/web/season/section?season_id=" + sid, JSONObject.class);
                JSONArray episodes = epData.get("result", JSONObject.class).get("main_section", JSONObject.class).get("episodes", JSONArray.class);
                episodes.forEach(o -> {
                    JSONObject episode = (JSONObject) o;
                    Long epid = episode.get("id", Long.class);
                    String title = episode.get("title", String.class);
                    episodeMapper.insert(new Episode(epid, 0, title, sid));
                });
            } catch (Exception exception) {
                System.out.println(exception);
                System.out.println(sid);
            }
        });
    }

    @Test
    public void test() {
        AbstractDownLoad abstractDownLoad = new AbstractDownLoad() {
            @Override
            public void save(JSONObject data) {
                JSONArray animeArray = data.get("data", JSONObject.class).get("list", JSONArray.class);
                animeArray.forEach(e -> {
                    JSONObject animeData = (JSONObject) e;
                    Long sid = animeData.get("season_id", Long.class);
                    List<Episode> ep = episodeMapper.selectList(new QueryWrapper<Episode>().eq("anime_id", sid));
                    if (ep!=null && ep.size()>0) {
                        return;
                    }
                    System.out.println(sid);
                    try {
                        JSONObject epData = restTemplate.getForObject("https://api.bilibili.com/pgc/web/season/section?season_id=" + sid, JSONObject.class);
                        JSONArray episodes = epData.get("result", JSONObject.class).get("main_section", JSONObject.class).get("episodes", JSONArray.class);
                        episodes.forEach(o -> {
                            JSONObject episode = (JSONObject) o;
                            Long epid = episode.get("id", Long.class);
                            String title = episode.get("title", String.class);
                            episodeMapper.insert(new Episode(epid, 0, title, sid));
                        });
                    } catch (Exception exception) {
                        System.out.println(exception);
                        System.out.println(sid);
                    }
                });
            }
        };
        abstractDownLoad.download();
    }
}