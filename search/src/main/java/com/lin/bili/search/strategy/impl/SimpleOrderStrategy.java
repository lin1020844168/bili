package com.lin.bili.search.strategy.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.lin.bili.common.utils.PageUtils;
import com.lin.bili.search.constant.EsConstant;
import com.lin.bili.search.dto.BaseAnimeDto;
import com.lin.bili.search.parser.Impl.FilterParamParse;
import com.lin.bili.search.po.EsAnimePo;
import com.lin.bili.search.strategy.OrderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SimpleOrderStrategy implements OrderStrategy {
    @Autowired
    private ElasticsearchClient esClient;

    @Autowired
    private FilterParamParse filterParamParse;

    @Override
    public PageUtils order(Map<String, String> params, int pageCnt, int pageSize) throws IOException, ParseException {
        List<Query> mustQueryList = filterParamParse.parse(params);
        String order = filterParamParse.parseOrder(params);
        SearchResponse<EsAnimePo> response = esClient.search(s -> s.index(EsConstant.ES_INDEX_ANIME).query(
                q -> q.bool(
                        b -> b.must(mustQueryList)
                )
        ).size(pageSize).from((pageCnt-1)*pageSize).sort(
                s2->s2.field(
                        f->f.field(order).order(SortOrder.Desc)
                )
        ), EsAnimePo.class);
        int total = (int)response.hits().total().value();
        List<BaseAnimeDto> baseAnimeDtoList = new ArrayList<>();
        response.hits().hits().forEach(e -> {
            EsAnimePo source = e.source();
            baseAnimeDtoList.add(new BaseAnimeDto(source.getId(), source.getCover(),
                    source.getTitle(), source.getSeason()));
        });
        PageUtils pageUtils = new PageUtils();
        pageUtils.setPageSize(pageSize);
        pageUtils.setTotalCount(total);
        pageUtils.setPageIndex(pageCnt);
        pageUtils.setTotalPage();
        pageUtils.setList(baseAnimeDtoList);
        return pageUtils;

    }
}
