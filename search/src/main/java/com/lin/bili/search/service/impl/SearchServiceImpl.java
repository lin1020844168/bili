package com.lin.bili.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.lin.bili.common.utils.PageUtils;
import com.lin.bili.search.constant.EsConstant;
import com.lin.bili.common.constant.RedisConstant;
import com.lin.bili.search.dto.BaseAnimeDto;
import com.lin.bili.search.exception.server.NotSuchStrategyException;
import com.lin.bili.search.exception.server.StrategyNotInitException;
import com.lin.bili.search.po.EsCompletionPo;
import com.lin.bili.search.service.SearchService;
import com.lin.bili.search.po.EsAnimePo;
import com.lin.bili.search.strategy.impl.SearchContext;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ElasticsearchClient esClient;

    @Autowired
    private SearchContext searchContext;

    private final String HIGHLIGHT_PRE = "<em style='color:red;'>";
    private final String HIGHLIGHT_POST = "</em>";


    @Override
    public PageUtils<BaseAnimeDto> listAnimeByKeyword(String keyword, Integer pageCnt, Integer pageSize) throws IOException {
        SearchResponse<EsAnimePo> response = esClient.search(s -> s.index(EsConstant.ES_INDEX_ANIME).query(
                q -> q.match(
                        m -> m.field("title").query(keyword)
                )
        ).size(pageSize).from((pageCnt - 1) * pageSize).highlight(
                h -> h.fields("title",
                        f -> f.preTags(HIGHLIGHT_PRE).postTags(HIGHLIGHT_POST))
        ), EsAnimePo.class);
        List<EsAnimePo> esAnimePoList = new ArrayList<>();
        int total = (int)response.hits().total().value();
        response.hits().hits().forEach(e -> {
            EsAnimePo source = e.source();
            esAnimePoList.add(source);
        });
        List<BaseAnimeDto> baseAnimeDtoList = esAnimePoList.stream()
                .map(e -> new BaseAnimeDto(e.getId(), e.getCover(), e.getTitle(), e.getSeason()))
                .collect(Collectors.toList());
        PageUtils<BaseAnimeDto> pageUtils = new PageUtils<>();
        pageUtils.setList(baseAnimeDtoList);
        pageUtils.setTotalCount(total);
        pageUtils.setPageIndex(pageCnt);
        pageUtils.setPageSize(pageSize);
        pageUtils.setTotalPage();
        return pageUtils;
    }

    @Override
    public List<String> hot(int count) {
        Set<String> hotWords = redisUtils.zRange(RedisConstant.HOT_WORD, -count, -1);
        ArrayList<String> handledHotWords = new ArrayList<>(hotWords);
        Collections.reverse(handledHotWords);
        return handledHotWords;
    }

    @Override
    public void addAnime(EsAnimePo esAnimePo) throws IOException {
        esClient.create(c -> c.index(EsConstant.ES_INDEX_ANIME).id(esAnimePo.getId().toString()).document(esAnimePo));
    }

    @Override
    public void bulkAddAnime(List<EsAnimePo> esAnimePoList) throws IOException {
        esClient.bulk(b -> {
            esAnimePoList.forEach(e -> {
                b.operations(o -> o.create(
                        c-> c.index(EsConstant.ES_INDEX_ANIME).id(e.getId().toString()).document(e)
                ));
            });
            return b;
        });
    }

    @Override
    public void updateAnime(EsAnimePo esAnimePo) throws IOException {
        esClient.update(u -> u.index(EsConstant.ES_INDEX_ANIME).id(esAnimePo.getId()
                .toString()).doc(esAnimePo), EsAnimePo.class);
    }

    @Override
    public void bulkUpdateAnime(List<EsAnimePo> esAnimePoList) throws IOException {
        esClient.bulk(b -> {
            esAnimePoList.forEach(e -> {
                b.operations(o->o.update(
                        u->u.index(EsConstant.ES_INDEX_ANIME).id(e.getId().toString()).action(
                                a->a.doc(e)
                        )
                ));
            });
            return b;
        });
    }

    @Override
    public List<String> complement(String keyword) throws IOException {
        SearchResponse<EsCompletionPo> response = esClient.search(s -> s.index(EsConstant.ES_INDEX_COMPLETION).query(
                q -> q.bool(
                        b -> b.should(
                                s1 -> s1.match(
                                        m -> m.field("keyword").query(keyword)
                                )
                        ).should(
                                s1 -> s1.prefix(
                                        p -> p.field("keyword.keyword_pinyin").value(keyword)
                                )
                        ))
        ).size(10).suggest(
                s2 -> s2.suggesters("completion_suggest",
                        s3 -> s3.prefix(keyword).completion(
                                c-> c.field("keyword.keyword_completion").size(10)
                        ))).highlight(
                h -> h.fields("keyword",
                        f-> f.preTags(HIGHLIGHT_PRE).postTags(HIGHLIGHT_POST))), EsCompletionPo.class);
        List<String> complementList = new ArrayList<>();
        Set<String> set = new HashSet<>();
        response.suggest().get("completion_suggest").forEach(e -> {
            e.completion().options().forEach(e2 -> {
                EsCompletionPo source = e2.source();
                if (complementList.size()<10) {
                    String completion = source.getKeyword();
                    String highlight = highlight(keyword, completion);
                    set.add(completion);
                    complementList.add(highlight);
                }

            });
        });
        response.hits().hits().forEach(e -> {
            String completion = e.source().getKeyword();
            if (set.contains(completion)) {
                return;
            }
            if (e.highlight().containsKey("keyword")) {
                completion = e.highlight().get("keyword").get(0);
            }
            if (complementList.size()<10) {
                complementList.add(completion);
                set.add(e.source().getKeyword());
            }
        });
        return complementList;
    }

    private String highlight(String keyword, String completion) {
        String substring = completion.substring(keyword.length());
        return HIGHLIGHT_PRE+keyword+HIGHLIGHT_POST+substring;
    }

    @Override
    public void addComplement(String keyword) throws IOException {
        esClient.create(c -> c.index(EsConstant.ES_INDEX_COMPLETION).document(new EsCompletionPo(keyword)));
    }

    @Override
    public void bulkAddComplement(List<String> keywordList) throws IOException {
        esClient.bulk(b -> {
            keywordList.forEach(e -> {
                b.operations(o -> o.create(
                        c-> c.index(EsConstant.ES_INDEX_COMPLETION).document(new EsCompletionPo(e))
                ));
            });
            return b;
        });
    }

    // param:partition=1, page=1, order=3, season_version=-1, spoken_language_type=-1,
    // area=-1, is_finish=-1, season_month=-1, year=-1, style_id=-1
    @Override
    public PageUtils<BaseAnimeDto> filterAnime(Map<String, String> params, int pageCnt, int pageSize) throws NotSuchStrategyException, StrategyNotInitException, IOException, ParseException {
        searchContext.chooseStrategy(params);
        PageUtils pageUtils = searchContext.search(params, pageCnt, pageSize);
        return pageUtils;
    }
}
