package com.lin.bili.anime.po.download;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractDownLoad {
    private RestTemplate restTemplate = new RestTemplate();

    public void download() {
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i=0; i<40; i++) {
            int tmp = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                int page = tmp+1;
                int pageSize = 84;
                JSONObject data = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=-1&st=1&sort=0&page="+page+"&season_type=1&pagesize="+pageSize+"&type=1", JSONObject.class);
                int cnt = 3;
                save(data);
            }, threadPool);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    }

    protected abstract void save(JSONObject data);

    protected String getJsonById(String mid) {
        Connection connect = Jsoup.connect("https://www.bilibili.com/bangumi/media/md" + mid);
        Document document = null;
        while (true) {
            try {
                document = connect.get();
                break;
            } catch (IOException e) {
                System.out.println("连接失败重试");
            }
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
