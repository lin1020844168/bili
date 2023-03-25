package com.lin.bili.jsuop.acfun.mutidownload;

import com.lin.bili.jsuop.acfun.po.Anime;
import com.lin.bili.jsuop.acfun.utils.AcFunUtils;
import com.lin.bili.jsuop.acfun.utils.ProxyPool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class AnimeDownLoadTask extends DownLoadTask<Anime> {
    public AnimeDownLoadTask(int start, int end, List<Anime> data, List<String> aids) {
        super(start, end, data, aids);
    }

    @Override
    public void doDownload(int i) {
        String animeBaseUrl = "http://www.acfun.cn/bangumi/aa";
        String url = animeBaseUrl+this.aids.get(i);
        int times = 5;
        while (times > 0) {
            Document doc = null;
            try {
//                String proxy = ProxyPool.randomProxy();
//                String host = proxy.split(":")[0];
//                String port = proxy.split(":")[1];
                doc = Jsoup.connect(url).
                        timeout(5000).
//                        userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36").
//                        proxy(host, Integer.parseInt(port)).
                        get();
                Anime anime = new Anime();
                String introduction = doc.getElementsByClass("description-container").get(0).text();
                String stateDetail = doc.getElementsByClass("update-status").get(0).text();
                int state = AcFunUtils.parseState(stateDetail);
                int total = AcFunUtils.parseTotal(stateDetail);
                int coinCnt = 0;
                int goodCnt = 0;
                int playCnt = 0;
                int collectCnt = 0;
                String name = doc.getElementsByClass("part-title").get(0).text();
                Date gmtCreate = new Date();
                Date gmtModified = new Date();
                anime.setIntroduction(introduction);
                anime.setState(state);
                anime.setCoinCnt(coinCnt);
                anime.setGoodCnt(goodCnt);
                anime.setPlayCnt(playCnt);
                anime.setCollectionCnt(collectCnt);
                anime.setName(name);
                anime.setGmtCreate(gmtCreate);
                anime.setGmtModified(gmtModified);
                anime.setTotal(total);
                data.add(anime);
                break;
            } catch (IOException e) {
                System.out.println(e.getMessage()+"重试");
                times--;
            }
        }
    }
}
