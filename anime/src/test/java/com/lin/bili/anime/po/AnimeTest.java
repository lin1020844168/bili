package com.lin.bili.anime.po;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lin.bili.anime.AnimeApplication;
import com.lin.bili.anime.service.AnimeService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(classes = AnimeApplication.class)
@RunWith(SpringRunner.class)
public class AnimeTest {
    @Autowired
    AnimeService animeService;

    RestTemplate restTemplate = new RestTemplate();

    ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    @Test
    public void test() {
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i=0; i<40; i++) {
            int tmp = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                int page = tmp+1;
                int pageSize = 84;
                JSONObject data = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=-1&st=1&sort=0&page="+page+"&season_type=1&pagesize="+pageSize+"&type=1", JSONObject.class);
                save(data);
                }, threadPool);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    }

    private void save(JSONObject data) {
        JSONArray animeArray = data.get("data", JSONObject.class).get("list", JSONArray.class);
        animeArray.forEach(e -> {
            JSONObject animeData = (JSONObject) e;
            Long sid = animeData.get("season_id", Long.class);
            if (animeService.getAnime(sid)!=null) return;
            String mid = animeData.get("media_id", String.class);
            String json = getJsonById(mid);
            JSONObject animeDetail = JSONUtil.parseObj(json).get("mediaInfo", JSONObject.class);
            Long id = animeDetail.get("season_id", Long.class);
            String cover = animeDetail.get("cover", String.class);
            String title = animeDetail.get("title", String.class);
            String desc = animeDetail.get("evaluate", String.class);
            Date pubTime = null;
            try {
                pubTime = threadLocal.get().parse(animeDetail.get("publish", JSONObject.class).get("pub_date", String.class));
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            Integer regionId = 0;
            if (animeDetail.containsKey("areas") && !animeDetail.get("areas", JSONArray.class).isEmpty()) {
                regionId=((JSONObject) animeDetail.get("areas", JSONArray.class).get(0)).get("id", Integer.class);
            }

            String author = parseStaff(animeDetail.get("staff", String.class));
            String season = animeDetail.get("publish", JSONObject.class).get("time_length_show", String.class);
            Integer rate = 0;
            if (animeDetail.containsKey("rating")) {
                rate = (int)(animeDetail.get("rating", JSONObject.class).get("score", Double.class)*10);
            }
            String[] actorStrs = animeDetail.get("actors", String.class).split("\\n");
            StringBuffer actors = new StringBuffer();
            Arrays.stream(actorStrs).map(s -> parseActor(s)).forEach(s -> actors.append(s+" "));
        });
    }

    private String parseActor(String s) {
        for (int i=0; i<s.length(); i++) {
            if (s.charAt(i)==':' || s.charAt(i)=='：') {
                return s.substring(i+1);
            }
        }
        return s;
    }

    private String parseStaff(String staff) {
        int l = 0;
        int r = 0;
        for (int i=0; i<staff.length(); i++) {
            if (staff.charAt(i) == '：' || staff.charAt(i)==':') {
                l = i + 1;
            }
            if (l > 0 && (staff.charAt(i) == '（' || staff.charAt(i) == '\n')) {
                r = i;
                break;
            }
        }
        return r>l?staff.substring(l, r):"";
    }

    private String getJsonById(String mid) {
        Connection connect = Jsoup.connect("https://www.bilibili.com/bangumi/media/md" + mid);
        Document document = null;
        try {
            document = connect.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String script = document.getElementsByTag("script").get(6).toString();
        String json = parseScript(script);
        return json;
    }

    private String parseScript(String targetScript) {
        int l = 0;
        int r = 0;
        for (int i=0; i<targetScript.length(); i++) {
            if (l==0 && targetScript.charAt(i)=='{') {
                l = i;
                continue;
            }
            if (targetScript.charAt(i)==';' && targetScript.charAt(i-1)=='}') {
                r = i;
                break;
            }
        }
        return targetScript.substring(l, r);
    }
}