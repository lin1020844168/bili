package com.lin.bili.anime.update;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lin.bili.anime.mapper.AnimeMapper;
import com.lin.bili.anime.po.Anime;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 增加需求时更改数据库数据
 */
public abstract class AbstractAnimeInserter {
    private AnimeMapper animeMapper;

    private RestTemplate restTemplate;

    private ExecutorService threadPool;

    private int threadSize = 2*8+1;

    public AbstractAnimeInserter(AnimeMapper animeMapper, RestTemplate restTemplate) {
        this.animeMapper = animeMapper;
        this.restTemplate = restTemplate;
        threadPool = Executors.newFixedThreadPool(threadSize);
    }

    protected abstract void doInsert(JSONObject data, Long animeId);

    protected void insert() {
        List<Anime> animeList = animeMapper.selectAll();
        List<CompletableFuture> futureList = new ArrayList<>();
        int bucketCnt = threadSize;
        int remainder = animeList.size()%bucketCnt;
        int bucketSize = animeList.size()/bucketCnt+1;
        for (int i=0; i<animeList.size(); i+=bucketSize) {
            if (remainder-- == 0) {
                bucketSize--;
            }
            int finalI = i;
            int finalBucketSize = bucketSize;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = finalI; j<finalI+ finalBucketSize; j++) {
                    Anime anime = animeList.get(j);
                    Long animeId = anime.getId();
                    JSONObject data = restTemplate.getForObject("http://api.bilibili.com/pgc/view/web/season?season_id=" + animeId, JSONObject.class);
                    try {
                        doInsert(data, animeId);
                    } catch (Exception exception) {
                        System.out.println("animeId:"+animeId);
                        System.out.println(exception);
                    }
                }
            }, threadPool);
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
    }
}
