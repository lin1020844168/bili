package com.lin.bili.anime.update;

import cn.hutool.json.JSONObject;
import com.lin.bili.common.utils.ParseBilibiliUtils;
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
    private ExecutorService threadPool;
    private int threadSize = 8+1;

    public AbstractDownLoad() {
        threadPool = Executors.newFixedThreadPool(threadSize);
    }

    public void download() {
        JSONObject initData = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=-1&st=1&sort=0&page=1&season_type=1&pagesize=1&type=1", JSONObject.class);
        Integer total = initData.get("data", JSONObject.class).get("total", Integer.class);
        int pageCnt = threadSize;
        int pageSize = total/pageCnt+1;
        int remainder = total%pageCnt;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        for (int i=0, cnt=0; i<total; i+=pageSize, cnt++) {
            if (remainder--==0) {
                pageSize--;
            }
            int tmp = i;
            int finalPageSize = pageSize;
            int finalCnt = cnt;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                int page = finalCnt +1;
                JSONObject data = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=-1&st=1&sort=0&page="+page+"&season_type=1&pagesize="+finalPageSize+"&type=1", JSONObject.class);
                save(data);
            }, threadPool);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    }

    protected abstract void save(JSONObject data);

    public String getJsonById(String mid) {
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
        String json = ParseBilibiliUtils.parseMediaScript(script);
        return json;
    }
}
