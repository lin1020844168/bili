package com.lin.bili.jsuop.acfun.mutidownload;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lin.bili.jsuop.acfun.mapper.AnimeMapper;
import com.lin.bili.jsuop.acfun.po.Anime;
import com.lin.bili.jsuop.acfun.po.AnimeVideo;
import com.lin.bili.jsuop.acfun.po.Video;
import com.lin.bili.jsuop.acfun.utils.AcFunUtils;
import com.lin.bili.jsuop.acfun.utils.ApplicationContextUtils;
import com.lin.bili.jsuop.acfun.vo.AnimeVideoUp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AnimeVideoDownLoadTask extends DownLoadTask {
    private AnimeMapper animeMapper;

    public AnimeVideoDownLoadTask(int start, int end, List data, List aids) {
        super(start, end, data, aids);
        AnimeMapper animeMapper = ApplicationContextUtils.getBean(AnimeMapper.class);
        this.animeMapper = animeMapper;
    }

    @Override
    public void doDownload(int i) {
        String animeBaseUrl = "http://www.acfun.cn/bangumi/aa";
        String url = animeBaseUrl + this.aids.get(i);
        int times = 5;
        while (times > 0) {
            Document doc = null;
            try {
                doc = Jsoup.connect(url).timeout(5000).get();
                String name = doc.getElementsByClass("part-title").get(0).text();
                QueryWrapper<Anime> queryWrapper = new QueryWrapper();
                queryWrapper.eq("name", name);
                Anime anime = animeMapper.selectOne(queryWrapper);
                Element script = doc.getElementsByTag("script").get(8);
                String[] split = script.toString().split("=");
                String[] strs = Arrays.stream(split).map(e -> e.trim()).filter(e -> e.startsWith("{\"result\"")).toArray(String[]::new);
                String json = AcFunUtils.parseJs(strs[0]);
                JSONArray items = JSONUtil.parseObj(json).getJSONArray("items");
                for (Object o : items) {
                    JSONObject item = (JSONObject) o;
                    String episodeName = item.get("episodeName", String.class);
                    int cur = AcFunUtils.parseEpisodeName(episodeName);
                    String title = item.get("title", String.class);
                    int partitionId = 1;
                    int animeId = anime.getId();
                    long videoId = IdUtil.getSnowflakeNextId();
                    AnimeVideo animeVideo = new AnimeVideo();
                    animeVideo.setAnimeId(animeId);
                    animeVideo.setVideoId(videoId);
                    animeVideo.setCommentCnt(0);
                    animeVideo.setCur(cur);
                    animeVideo.setGmtCreate(new Date());
                    animeVideo.setGmtModified(new Date());
                    Video video = new Video();
                    video.setId(videoId);
                    video.setTitle(title);
                    video.setPartitionId(partitionId);
                    video.setGmtCreate(new Date());
                    video.setGmtModified(new Date());
                    AnimeVideoUp animeVideoUp = new AnimeVideoUp(null, animeVideo, video);
                    data.add(animeVideoUp);
                    System.out.println(animeVideoUp);
                }
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage() + "重试");
                times--;
            }
        }
    }
}
