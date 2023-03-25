package com.lin.bili.jsuop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lin.bili.common.constant.HttpContent;
import com.lin.bili.jsuop.dto.*;
import com.lin.bili.jsuop.service.BiliBIliService;
import com.lin.bili.jsuop.vo.AnimeDetailVo;
import com.lin.bili.jsuop.vo.FilterVo;
import com.lin.bili.jsuop.vo.IndexVo;
import com.lin.bili.jsuop.vo.SearchVo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BiliBIliServiceImpl implements BiliBIliService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HttpEntity httpEntity;

    private final String IMG_PRE = "";

    private final String VIDEO_ORG = "http://127.0.0.1:7003/";

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 7, 20, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    /**
     * 获取首页数据
     * @return
     * @throws IOException
     */
    @Override
    public IndexVo getIndex() throws IOException {
        IndexVo index = new IndexVo();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        CompletableFuture<Void> bannerFuture = CompletableFuture.runAsync(() -> {
            try {
                index.setBanner(getBanner());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, threadPool);
        CompletableFuture<Void> chineseFuture = CompletableFuture.runAsync(() -> {
            try {
                List<IndexVo.ChineseComic> chineseComic = getChineseComic();
                index.setChinese_comic(chineseComic);
            } catch (Exception e) {
                e.printStackTrace();
                index.setChinese_comic(new ArrayList<>());
            }
        }, threadPool);
        CompletableFuture<Void> HotFuture = CompletableFuture.runAsync(() -> {
            try {
                List<IndexVo.Hot> results = getHot();
                Map<String, List<IndexVo.Hot>> hots = new HashMap<>();
                hots.put("results", results);
                index.setHots(hots);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, threadPool);
        CompletableFuture<Void> lastFuture = CompletableFuture.runAsync(() -> {
            index.setLatest(getLatest());
        }, threadPool);
        CompletableFuture<Void> japanFuture = CompletableFuture.runAsync(() -> {
            index.setJapancomic(getJapanComic());
        }, threadPool);
        CompletableFuture<Void> perweekFuture = CompletableFuture.runAsync(() -> {
            index.setPerweek(getPerweek());
        }, threadPool);
        CompletableFuture<Void> theatreFuture = CompletableFuture.runAsync(() -> {
            index.setTheatre_comic(getTheatreComic());
        }, threadPool);
        Collections.addAll(futures, theatreFuture,perweekFuture, japanFuture, lastFuture, HotFuture, chineseFuture, bannerFuture);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return index;
    }

    /**
     * 关键字搜索
     * @param name
     * @param page
     * @return
     * @throws IOException
     */
    @Override
    public SearchVo search(String name, int page) throws IOException {
        HttpHeaders header = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add(HttpContent.BILIBILI_COOKIE);
        header.put(HttpHeaders.COOKIE, cookies);
        HttpEntity requestEntity = new HttpEntity(header);
        ResponseEntity<JSONObject> exchange = restTemplate.exchange("https://api.bilibili.com/x/web-interface/search/type?__refresh__=true&page="+page+"&page_size=12&platform=pc&highlight=1&single_column=0&keyword="+ name +"&qv_id=HKtkplPGx7fOgJQCEOfJu7FZck9lRH2G&source_tag=3&search_type=media_bangumi",
                HttpMethod.GET, requestEntity, JSONObject.class);
        JSONObject resp = exchange.getBody();
        SearchVo search = new SearchVo();
        int size = resp.get("data", JSONObject.class).get("pagesize", Integer.class);
        int total= resp.get("data", JSONObject.class).get("numResults", Integer.class);
        JSONArray metaArray = resp.get("data", JSONObject.class).get("result", JSONArray.class);
        search.setSize(size);
        search.setTotal(total);
        List<SearchVo.Result> results = new ArrayList<>();
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            String id = metadata.get("season_id", String.class);
            String category = metadata.get("styles", String.class);
            String title = metadata.get("title", String.class);
            String cover = metadata.get("cover", String.class);
            long timestamp = metadata.get("pubtime", Long.class);
            String date = DateUtil.format(new Date(timestamp), "yyyy-MM-DD");
            String description = metadata.get("desc", String.class);
            String season = "完结";
            results.add(new SearchVo.Result(category, cover, date, description, id, season, title));
        }
        search.setResults(results);
        return search;
    }

    @Override
    public ConfigDto getConfig() throws IOException {
        Connection connect = Jsoup.connect("https://www.bilibili.com/anime/index/#season_version=-1&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=3&st=1&sort=0&page=1");
        Document document = connect.get();
        String script = document.getElementsByTag("script").get(9).toString();
        String json = parseScript(script);
        JSONObject data = JSONUtil.parseObj(json);
        JSONObject styleData = (JSONObject) data.get("filters", JSONArray.class).get(8);
        List<ConfigDto.Category> styles = new ArrayList<>();
        for (Object o : styleData.get("list", JSONArray.class)) {
            JSONObject metadata = (JSONObject) o;
            int id = metadata.get("value", Integer.class);
            String name = metadata.get("name", String.class);
            styles.add(new ConfigDto.Category(name, id));
        }
        JSONObject stateData = (JSONObject) data.get("filters", JSONArray.class).get(3);
        List<ConfigDto.Type> categories = new ArrayList<>();
        for (Object o : stateData.get("list", JSONArray.class)) {
            JSONObject metadata = (JSONObject) o;
            int id = metadata.get("value", Integer.class);
            String name = metadata.get("name", String.class);
            categories.add(new ConfigDto.Type(name, id, styles));
        }
        ConfigDto getConfig = new ConfigDto(categories);
        return getConfig;
    }

    /**
     * 过滤搜索
     *
     * @param params@return
     */
    @Override
    public FilterVo filter(Map<String, String> params) {
        StringBuffer url = new StringBuffer("https://api.bilibili.com/pgc/season/index/result?st=1&pagesize=36&sort=0&type=1&season_type=1");
        params.forEach((k, v) -> {
            url.append("&"+k+"="+v);
        });
        FilterVo filter = new FilterVo();
        JSONObject data = restTemplate.getForObject(url.toString(), JSONObject.class);
        Integer size = data.get("data", JSONObject.class).get("size", Integer.class);
        Integer total = data.get("data", JSONObject.class).get("total", Integer.class);
        filter.setTotal(total);
        filter.setSize(size);
        JSONArray metaArray = data.get("data", JSONObject.class).get("list", JSONArray.class);
        List<FilterVo.Result> results = new ArrayList<>();
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            String cover = metadata.get("cover", String.class);
            String id = metadata.get("season_id", String.class);
            String title = metadata.get("title", String.class);
            String season = metadata.get("order", String.class);
            results.add(new FilterVo.Result(cover, id, season, title));
        }
        filter.setResults(results);
        return filter;
    }

    /**
     * 获取番剧详情
     * @param id
     * @return
     * @throws IOException
     */
    @Override
    public AnimeDetailVo getAnimeDetail(String id) throws IOException {
        JSONObject data = restTemplate.getForObject("http://api.bilibili.com/pgc/view/web/season?season_id=" + id, JSONObject.class);
        String mid = data.get("result", JSONObject.class).get("media_id", String.class);
        Connection connect = Jsoup.connect("https://www.bilibili.com/bangumi/media/md" + mid);
        Document document = connect.get();
        String script = document.getElementsByTag("script").get(6).toString();
        String json = parseScript(script);
        JSONObject mData = JSONUtil.parseObj(json).get("mediaInfo", JSONObject.class);
        String[] actors = Arrays.stream(mData.get("actors", String.class).split("\\n")).map(s->{
            return parseActor(s);
        }).toArray(String[]::new);
        String[] categories = mData.get("styles", JSONArray.class)
                .stream().map(o -> ((JSONObject) o).get("name", String.class)).toArray(String[]::new);
        String cover = mData.get("cover", String.class);
        String first_date = mData.get("publish", JSONObject.class).get("pub_date", String.class);
        JSONObject areas = (JSONObject) mData.get("areas", JSONArray.class).get(0);
        String lang = areas.get("name", String.class);
        String master = parseStaff(mData.get("staff", String.class)) ;
        String rank =  mData.containsKey("rating")?mData.get("rating", JSONObject.class).get("score", String.class):"";
        String region = lang;
        String season = mData.get("publish", JSONObject.class).get("time_length_show", String.class);
        String title = mData.get("title", String.class);
        Map<String, List<AnimeDetailVo.Play>> playList = getPlayList(id);
        AnimeDetailVo getAnime = new AnimeDetailVo(actors, categories, cover, first_date, lang, master, playList, rank, region, season, title);
        return getAnime;
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

    @Override
    public EpisodeOrgDto getEpisodeOrg(String id) {
        JSONObject data = restTemplate.getForObject("https://api.bilibili.com/pgc/web/season/section?season_id=" + id, JSONObject.class);
        JSONArray episodeData = data.get("result", JSONObject.class).get("main_section", JSONObject.class).get("episodes", JSONArray.class);
        List<EpisodeOrgDto> episodeOrgs = new ArrayList<>();
        List<EpisodeOrgDto.Quality> qualities = new ArrayList<>();
        List<EpisodeOrgDto.Episode> episodes = new ArrayList<>();
        for (Object o : episodeData) {
            JSONObject episode = (JSONObject) o;
            String eid = episode.get("id", String.class);
            String cur = episode.get("title", String.class);
            String title = episode.get("long_title", String.class);
            episodes.add(new EpisodeOrgDto.Episode(eid, cur, title));
        }
        String eid = episodes.size()==1?episodes.get(0).getId():episodes.get(1).getId();
        ResponseEntity<JSONObject> resp = restTemplate.exchange("https://api.bilibili.com/pgc/player/web/playurl?fnver=0&fnval=4048&ep_id=" + eid,
                HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject qualitiesData = resp.getBody();
        List<Integer> qualityValues = qualitiesData.get("result", JSONObject.class).get("accept_quality", JSONArray.class)
                .stream().map(e -> Integer.parseInt(e.toString()) ).collect(Collectors.toList());
        List<String> qualityDescs = qualitiesData.get("result", JSONObject.class).get("accept_description", JSONArray.class)
                .stream().map(e -> e.toString()).collect(Collectors.toList());
        for (int i=0; i<qualityValues.size()&&i<qualityDescs.size(); i++) {
            qualities.add(new EpisodeOrgDto.Quality(qualityValues.get(i), qualityDescs.get(i)));
        }
        EpisodeOrgDto episodeOrg = new EpisodeOrgDto("bilibili", "http://127.0.0.1:7003/video/bilibili", qualities, episodes);
        return episodeOrg;
    }

    @Override
    public VideoAudioUrlDto getEpisodeUrl(String id, String quality) {
        ResponseEntity<JSONObject> resp = restTemplate
                .exchange("https://api.bilibili.com/pgc/player/web/playurl?fnver=0&fnval=4048&ep_id=" + id +"&qn="+quality,
                        HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject data = resp.getBody();
        JSONArray videos = data.get("result", JSONObject.class).get("dash", JSONObject.class)
                .get("video", JSONArray.class);
        JSONObject[] targetVideos = videos.stream().filter((e) -> ((JSONObject) e).get("id", String.class).equals(quality) ).toArray(JSONObject[]::new);
        JSONObject video = targetVideos[1];
        String videoUrl = video.get("backupUrl", JSONArray.class).get(0).toString();
        JSONArray audios = data.get("result", JSONObject.class).get("dash", JSONObject.class)
                .get("audio", JSONArray.class);
        JSONObject audio = (JSONObject) (audios.get(1));
        String audioUrl = audio.get("backupUrl", JSONArray.class).get(0).toString();
        VideoAudioUrlDto videoAudioUrl = new VideoAudioUrlDto(videoUrl, audioUrl);
        return videoAudioUrl;
    }

    /**
     * 获取分集相关信息
     * @param episodeId
     * @return
     */
    @Override
    public EpisodeDto getEpisode(String episodeId) {
        JSONObject data = restTemplate.getForObject("http://api.bilibili.com/pgc/view/web/season?ep_id=" + episodeId, JSONObject.class);
        String animeId = data.get("result", JSONObject.class).get("season_id", String.class);
        String cur = "";
        JSONArray episodes = data.get("result", JSONObject.class).get("episodes", JSONArray.class);
        for (Object o : episodes) {
            JSONObject episode = (JSONObject) o;
            if (episodeId.equals(episode.get("id", String.class))) {
                cur = episode.get("title", String.class);
                break;
            }
        }
        return new EpisodeDto(animeId, cur);
    }

    /**
     * 获取搜索分类条目
     * @return
     */
    @Override
    public List<SearchCategoryDto> getCategoryConfig() throws IOException {
        Connection connect = Jsoup.connect("https://www.bilibili.com/anime/index/#season_version=-1&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=3&st=1&sort=0&page=1");
        Document document = connect.get();
        String script = document.getElementsByTag("script").get(9).toString();
        String json = parseScript(script);
        JSONObject data = JSONUtil.parseObj(json);
        List<SearchCategoryDto> roots = new ArrayList<>();
        List<SearchCategoryDto> animeChildren = new ArrayList<>();
        roots.add(new SearchCategoryDto("番剧", "1", animeChildren));
        List<SearchCategoryDto> orderChildren = new ArrayList<>();
        JSONArray orders = data.get("orders", JSONArray.class);
        orders.forEach(e -> {
            JSONObject order = (JSONObject) e;
            String name = order.get("title", String.class);
            String value = order.get("key", String.class);
            orderChildren.add(new SearchCategoryDto(name, value, null));
        });
        animeChildren.add(new SearchCategoryDto("排序", "order", orderChildren));
        JSONArray filters = data.get("filters", JSONArray.class);
        filters.forEach(e -> {
            JSONObject filter = (JSONObject) e;
            String name = filter.get("title", String.class);
            String value = filter.get("key", String.class);
            JSONArray childArray = filter.get("list", JSONArray.class);
            List<SearchCategoryDto> children = new ArrayList<>();
            childArray.forEach(o -> {
                JSONObject child = (JSONObject) o;
                String childName = child.get("name", String.class);
                String childValue = child.get("value", String.class);
                children.add(new SearchCategoryDto(childName, childValue, null));
            });
            animeChildren.add(new SearchCategoryDto(name, value, children));
        });
        return roots;
    }

    /**
     * 获取默认播放源列表
     * @param id
     * @return
     */
    private Map<String, List<AnimeDetailVo.Play>> getPlayList(String id) {
        Map<String, List<AnimeDetailVo.Play>> playList = new HashMap<>();
        List<AnimeDetailVo.Play> plays = new ArrayList<>();
        JSONObject data = restTemplate.getForObject("https://api.bilibili.com/pgc/web/season/section?season_id=" + id, JSONObject.class);
        JSONArray episodes = data.get("result", JSONObject.class).get("main_section", JSONObject.class).get("episodes", JSONArray.class);
        for (Object o : episodes) {
            JSONObject episode = (JSONObject) o;
            String epid = episode.get("id", String.class);
            plays.add(new AnimeDetailVo.Play(VIDEO_ORG+epid+"/", episode.get("title", String.class)));
        }
        playList.put("bilibili", plays);
        return playList;
    }

    /**
     * 获取番外动画
     * @return
     */
    private List<IndexVo.TheatreComic> getTheatreComic() {
        JSONObject resp = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=3&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=3&st=1&sort=0&page=1&season_type=1&pagesize=20&type=1", JSONObject.class);
        JSONArray metaArray = resp.get("data", JSONObject.class).getJSONArray("list");
        List<IndexVo.TheatreComic> theatreComics = new ArrayList<>();
        int count = 6;
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            String cover = IMG_PRE+metadata.get("cover", String.class);
            String id = metadata.get("season_id", String.class);
            String season = "更新";
            String title = metadata.get("title", String.class);
            theatreComics.add(new IndexVo.TheatreComic(cover, id, season, title));
            if (--count == 0) break;
        }
        return theatreComics;
    }

    /**
     * 获取每周更信列表
     * @return
     */
    private Map<String, List<IndexVo.PerweekComic>> getPerweek() {
        JSONObject resp = restTemplate.getForObject("https://api.bilibili.com/pgc/web/timeline?types=1&before=6&after=0", JSONObject.class);
        JSONArray metaArray = resp.get("result", JSONArray.class);
        Map<String, List<IndexVo.PerweekComic>> perweek = new HashMap<>();
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            List<IndexVo.PerweekComic> perweekComics =  new ArrayList<>();
            JSONArray episodes = metadata.get("episodes", JSONArray.class);
            for (Object o1 : episodes) {
                JSONObject episode = (JSONObject) o1;
                String season = "更新";
                String id = episode.get("season_id", String.class);
                String title = episode.get("title", String.class);
                perweekComics.add(new IndexVo.PerweekComic(id, season, title));
            }
            String day = metadata.get("day_of_week", String.class);
            perweek.put((Integer.parseInt(day)-1)+"", perweekComics);
        }
        return perweek;
    }

    /**
     * 获取最新更新列表
     * @return
     */
    private List<IndexVo.Latest> getLatest() {
        JSONObject resp = restTemplate.getForObject("https://bangumi.bilibili.com/api/timeline_v2_global", JSONObject.class);
        JSONArray metaArray = resp.get("result", JSONArray.class);
        List<IndexVo.Latest> latests = new ArrayList<>();
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            String cover = IMG_PRE+metadata.get("cover", String.class);
            String id = metadata.get("season_id", String.class);
            String season = "更新";
            String title = metadata.get("title", String.class);
            latests.add(new IndexVo.Latest(cover, id, season, title));
        }
        return latests;
    }

    /**
     * 获取热门列表
     * @return
     * @throws IOException
     */
    private List<IndexVo.Hot> getHot() throws IOException {
        Connection connection = Jsoup.connect("https://www.bilibili.com/anime/");
        Document document = connection.get();
        String targetScript = document.getElementsByTag("script").get(9).toString();
        String dataString =  parseScript(targetScript);
        JSONObject data = JSONUtil.parseObj(dataString);
        JSONArray metaArray = data.getJSONArray("handPickList");
        List<IndexVo.Hot> hots = new ArrayList<>();
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            String cover = IMG_PRE+metadata.get("img", String.class);
            String id = parseLink(metadata.get("link", String.class));
            String title = metadata.get("title", String.class);
            String season = metadata.get("badge", String.class);
            hots.add(new IndexVo.Hot(cover, id, season, title, null, null));
        }
        return hots;
    }

    private String parseLink(String link) {
        int l = 0;
        int r = 0;
        for (int i=0; i<link.length(); i++) {
            if (l==0 && link.charAt(i)>='0' && link.charAt(i)<='9') {
                l = i;
            }
            if (l!=0 && !(link.charAt(i)>='0' && link.charAt(i)<='9')) {
                r = i;
                break;
            }
        }
        r = r==0?link.length():r;
        if (link.charAt(l-1)=='s') {
            return link.substring(l, r);
        }
        String epid = link.substring(l, r);
        JSONObject data = restTemplate.getForObject("http://api.bilibili.com/pgc/view/web/season?ep_id=" + epid, JSONObject.class);
        String id = data.get("result", JSONObject.class).get("season_id", String.class);
        return id;
    }

    /**
     * 获取热门国漫
     * @return
     * @throws IOException
     */
    private List<IndexVo.ChineseComic> getChineseComic() throws IOException {
        Connection connection = Jsoup.connect("https://www.bilibili.com/guochuang");
        Document document = connection.get();
        String targetScript = document.getElementsByTag("script").get(9).toString();
        String dataString =  parseScript(targetScript);
        JSONObject data = JSONUtil.parseObj(dataString);
        JSONArray metaArray = data.getJSONArray("handPickList");
        List<IndexVo.ChineseComic> chineseComics = new ArrayList<>();
        int count = 4;
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            String cover = IMG_PRE+metadata.get("img", String.class);
            String id = parseLink(metadata.get("link", String.class)) ;
            String title = metadata.get("title", String.class);
            String season = "热播中";
            chineseComics.add(new IndexVo.ChineseComic(cover, id, season, title));
            if (--count == 0) break;
        }
        return chineseComics;
    }

    /**
     * 获取轮播图
     * @return
     */
    private List<IndexVo.Banner> getBanner() throws IOException {
        Connection connection = Jsoup.connect("https://www.bilibili.com/anime/");
        Document document = connection.get();
        String targetScript = document.getElementsByTag("script").get(9).toString();
        String dataString =  parseScript(targetScript);
        JSONObject data = JSONUtil.parseObj(dataString);
        JSONArray metaArray = data.getJSONArray("carouselList");
        List<IndexVo.Banner> banners = new ArrayList<>();
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            try {
                String id = parseLink(metadata.get("link", String.class));
                String cover = IMG_PRE+metadata.get("img", String.class);
                String title = metadata.get("title", String.class);
                banners.add(new IndexVo.Banner(cover, id, title));
            } catch (Exception e) {
                log.error("链接解析失败", e);
            }
        }
        return banners;
    }

    /**
     * 解析script,得到json字符串
     * @param targetScript
     * @return
     */
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

    /**
     * 获取完结日漫
     * @return
     */
    private List<IndexVo.JapanComic> getJapanComic() {
        JSONObject data = restTemplate.getForObject("https://api.bilibili.com/pgc/web/rank/list?day=7&season_type=1", JSONObject.class);
        JSONArray metaArray = data.get("result", JSONObject.class).get("list", JSONArray.class);
        List<IndexVo.JapanComic> japanComics = new ArrayList<>();
        int count = 4;
        for (Object o : metaArray) {
            JSONObject metadata = (JSONObject) o;
            String cover = IMG_PRE+metadata.get("cover", String.class);
            String id = metadata.get("season_id", String.class);
            String season = "完结";
            String title = metadata.get("title", String.class);
            japanComics.add(new IndexVo.JapanComic(cover, id, season, title));
            if (--count==0) break;
        }
        return japanComics;
    }
}
