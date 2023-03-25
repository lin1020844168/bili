package com.lin.bili.jsuop.acfun.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.bili.jsuop.acfun.mapper.AnimeMapper;
import com.lin.bili.jsuop.acfun.mutidownload.AnimeDownLoadTask;
import com.lin.bili.jsuop.acfun.mutidownload.AnimeVideoDownLoadTask;
import com.lin.bili.jsuop.acfun.po.Anime;
import com.lin.bili.jsuop.acfun.po.AnimeVideo;
import com.lin.bili.jsuop.acfun.po.Video;
import com.lin.bili.jsuop.acfun.utils.AcFunUtils;
import com.lin.bili.jsuop.acfun.vo.AnimeVideoUp;
import com.mysql.cj.xdevapi.JsonArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AcFunParser {
    @Autowired
    AnimeMapper animeMapper;

    public List<Anime> getAnimeList() throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, InterruptedException {
        String baseUrl = "http://www.acfun.cn/bangumilist?filters=12,20,30,40,50,805306368&pageNum=";
        List<String> aids = new ArrayList<>();
        int start = 2;
        int multi = 4;
        for (int i=start*multi+1; i<=(start+1)*multi; i++) {
            String url = baseUrl+i;
            Document doc = Jsoup.connect(url).get();
            Elements es = doc.getElementsByClass("ac-mod-li");
            for (Element e : es) {
                aids.add(e.attr("data-aid"));
            }
        }
        System.out.println(aids.size());
        List<Anime> data = new CopyOnWriteArrayList<>();
        AcFunUtils.multithreadDownload(20, data, aids, AnimeDownLoadTask.class);
        return data;
    }

    public List<AnimeVideoUp> getAnimeVideoList() throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, InterruptedException {
        String baseUrl = "http://www.acfun.cn/bangumilist?filters=12,20,30,40,50,805306368&pageNum=";
        List<String> aids = new ArrayList<>();
        int start = 0;
        int multi = 1;
        for (int i=start*multi+1; i<=(start+1)*multi; i++) {
            String url = baseUrl+i;
            Document doc = Jsoup.connect(url).get();
            Elements es = doc.getElementsByClass("ac-mod-li");
            for (Element e : es) {
                aids.add(e.attr("data-aid"));
            }
        }
        List<AnimeVideoUp> data = new CopyOnWriteArrayList<>();
        AcFunUtils.multithreadDownload(20, data, aids, AnimeVideoDownLoadTask.class);
        return data;
    }
}
