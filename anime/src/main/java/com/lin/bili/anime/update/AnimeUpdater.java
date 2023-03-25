package com.lin.bili.anime.update;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lin.bili.anime.mapper.*;
import com.lin.bili.anime.po.*;
import com.lin.bili.common.utils.ParseBilibiliUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * 更新bilibili番剧和分集
 */
@Component
public class AnimeUpdater {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AnimeMapper animeMapper;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private AnimeActorMapper animeActorMapper;

    @Autowired
    private StyleMapper styleMapper;

    @Autowired
    private AnimeStyleMapper animeStyleMapper;

    @Autowired
    private EpisodeMapper episodeMapper;

    private ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));

    private AbstractDownLoad downLoader = new AbstractDownLoad() {
        @Override
        protected void save(JSONObject data) {
            doUpdate(data);
            updateSeasonVersion();
            updateSpokenLanguageTypeId();
        }
    };

    private void updateSpokenLanguageTypeId() {
        JSONObject initData = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=3&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=-1&st=1&sort=0&page=1&season_type=1&pagesize=1&type=1", JSONObject.class);
        Integer total = initData.get("data", JSONObject.class).get("total", Integer.class);
        JSONObject data = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=3&spoken_language_type=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=-1&st=1&sort=0&page=1&season_type=1&pagesize="+total+"&type=1", JSONObject.class);
        JSONArray animeArray = data.get("data", JSONObject.class).get("list", JSONArray.class);
        animeArray.forEach(e -> {
            JSONObject animeData = (JSONObject) e;
            Long animeId = animeData.get("season_id", Long.class);
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", animeId);
            updateWrapper.set("season_version_id", 3);
            animeMapper.update(null, updateWrapper);
        });
    }

    private void updateSeasonVersion() {
        JSONObject initData = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&spoken_language_type=2&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=-1&st=1&sort=0&page=1&season_type=1&pagesize=1&type=1", JSONObject.class);
        Integer total = initData.get("data", JSONObject.class).get("total", Integer.class);
        JSONObject data = restTemplate.getForObject("https://api.bilibili.com/pgc/season/index/result?season_version=-1&spoken_language_type=2&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=-1&st=1&sort=0&page=1&season_type=1&pagesize="+total+"&type=1", JSONObject.class);
        JSONArray animeArray = data.get("data", JSONObject.class).get("list", JSONArray.class);
        animeArray.forEach(e -> {
            JSONObject animeData = (JSONObject) e;
            Long animeId = animeData.get("season_id", Long.class);
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", animeId);
            updateWrapper.set("spoken_language_type_id",2);
            animeMapper.update(null, updateWrapper);
            });
    }



    @Transactional
    public void doUpdate(JSONObject data) {
        JSONArray animeArray = data.get("data", JSONObject.class).get("list", JSONArray.class);
        animeArray.forEach(e -> {
            JSONObject animeData = (JSONObject) e;
            Long animeId = animeData.get("season_id", Long.class);
            Anime anime = animeMapper.getById(animeId);
            if (existAnime(anime) && anime.getIsFinish()==1) {
                return;
            }
            if (!existAnime(anime)) {
                String mid = animeData.get("media_id", String.class);
                String json = downLoader.getJsonById(mid);
                JSONObject animeDetail = JSONUtil.parseObj(json).get("mediaInfo", JSONObject.class);
                Integer isStarted = animeDetail.get("publish",JSONObject.class).get("is_started", Integer.class);
                addAnime(animeDetail);
                addActor(animeDetail);
                addStyle(animeDetail);
                if (isStarted==1) {
                    addEpisode(animeId);
                }
                return;
            }
            if (anime.getIsFinish() == 0) {
                JSONObject result = restTemplate.getForObject("http://api.bilibili.com/pgc/view/web/season?season_id=" + animeId, JSONObject.class)
                        .get("result", JSONObject.class);
                Integer isStarted = result.get("publish", JSONObject.class).get("is_started", Integer.class);
                if (isStarted==1) {
                    updateAnime(animeData);
                    updateEpisode(animeId);
                }
            }
        });
    }

    @Transactional
    public void updateEpisode(Long animeId) {
        JSONObject epData = restTemplate.getForObject("https://api.bilibili.com/pgc/web/season/section?season_id=" + animeId, JSONObject.class);
        if (!epData.get("result", JSONObject.class).containsKey("main_section")) {
            return;
        }
        JSONArray episodes = epData.get("result", JSONObject.class).get("main_section", JSONObject.class).get("episodes", JSONArray.class);
        episodes.forEach(o -> {
            JSONObject episode = (JSONObject) o;
            Long epid = episode.get("id", Long.class);
            if (existEpisode(epid)) {
                return;
            }
            String title = episode.get("title", String.class);
            episodeMapper.insert(new Episode(epid, 0, title, animeId));
        });
    }

    private boolean existEpisode(Long epid) {
        return episodeMapper.selectById(epid)!=null;
    }

    @Transactional
    public void updateAnime(JSONObject animeData) {
        Long animeId = animeData.get("season_id", Long.class);
        Integer isFinish = animeData.get("is_finish", Integer.class);
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", animeId);
        updateWrapper.set("is_finish", isFinish);
        animeMapper.update(null, updateWrapper);
    }

    @Transactional
    public void addEpisode(Long animeId) {
        JSONObject epData = restTemplate.getForObject("https://api.bilibili.com/pgc/web/season/section?season_id=" + animeId, JSONObject.class);
        if (!epData.get("result", JSONObject.class).containsKey("main_section")) {
            return;
        }
        JSONArray episodes = epData.get("result", JSONObject.class).get("main_section", JSONObject.class).get("episodes", JSONArray.class);
        episodes.forEach(o -> {
            JSONObject episode = (JSONObject) o;
            Long epid = episode.get("id", Long.class);
            String title = episode.get("title", String.class);
            episodeMapper.insert(new Episode(epid, 0, title, animeId));
        });
    }

    @Transactional
    public void addActor(JSONObject animeDetail) {
        Long animeId = animeDetail.get("season_id", Long.class);
        Arrays.stream(animeDetail.get("actors", String.class).split("\\n"))
                .forEach((actorName -> {
                    actorName = ParseBilibiliUtils.parseActor(actorName);
                    Integer actorId;
                    synchronized (this) {
                        actorId = actorMapper.getIdByName(actorName);
                        if (actorId == null) {
                            actorMapper.insert(new Actor(null, actorName));
                            actorId = actorMapper.getIdByName(actorName);
                        }
                    }
                    try {
                        animeActorMapper.insert(new AnimeActor(animeId, actorId));
                    } catch (Exception exception) {
                        System.out.println("主键重复了--");
                    }
                }));
    }

    @Transactional
    public void addStyle(JSONObject animeDetail) {
        Long animeId = animeDetail.get("season_id", Long.class);
        animeDetail.get("styles", JSONArray.class).forEach(o -> {
            JSONObject style = (JSONObject) o;
            Integer id = style.get("id", Integer.class);
            String name = style.get("name", String.class);
            synchronized (this) {
                if (styleMapper.selectById(id)==null) {
                    styleMapper.insert(new Style(id, name));
                }
            }
            animeStyleMapper.insert(new AnimeStyle(animeId, id));
        });
    }

    @Transactional
    public void addAnime(JSONObject animeDetail) {
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

        String author = ParseBilibiliUtils.parseStaff(animeDetail.get("staff", String.class));
        String season = animeDetail.get("publish", JSONObject.class).get("time_length_show", String.class);
        Integer rate = 0;
        if (animeDetail.containsKey("rating")) {
            rate = (int)(animeDetail.get("rating", JSONObject.class).get("score", Double.class)*10);
        }
        Integer seasonVersionId = animeDetail.get("type", Integer.class);
        Integer spokenLanguageTypeId = 1;
        Integer isFinish = animeDetail.get("publish", JSONObject.class).get("is_finish", Integer.class);
        Integer partitionId = 1;
        Long favCount = animeDetail.get("stat", JSONObject.class).get("favorites", Long.class);
        Long playCount = animeDetail.get("stat", JSONObject.class).get("views", Long.class);
        Integer isStarted = animeDetail.get("publish", JSONObject.class).get("is_started", Integer.class);
        Anime anime = new Anime(id, cover, title, desc, pubTime, regionId, author, season, rate, seasonVersionId, spokenLanguageTypeId, isFinish, partitionId, favCount, playCount, isStarted);
        animeMapper.insert(anime);
    }

    private boolean existAnime(Anime anime) {
        return anime!=null;
    }

    public void update() {
        downLoader.download();
    }

}
