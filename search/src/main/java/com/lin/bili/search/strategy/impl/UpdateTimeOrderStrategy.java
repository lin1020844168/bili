package com.lin.bili.search.strategy.impl;

import co.elastic.clients.elasticsearch.xpack.usage.Base;
import com.lin.bili.common.utils.PageUtils;
import com.lin.bili.search.dto.BaseAnimeDto;
import com.lin.bili.search.dto.FilterVo;
import com.lin.bili.search.feign.JsoupFeign;
import com.lin.bili.search.strategy.OrderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateTimeOrderStrategy implements OrderStrategy {
    @Autowired
    private JsoupFeign jsoupFeign;

    @Override
    public PageUtils<BaseAnimeDto> order(Map<String, String> params, int pageCnt, int pageSize) {
        Map<String, String> map = mapper(params, pageCnt, pageSize);
        FilterVo data = jsoupFeign.filter(map).getData();
        List<FilterVo.Result> resultList = data.getResults();
        List<BaseAnimeDto> baseAnimeDtoList = resultList.stream().map(e -> new BaseAnimeDto(Long.parseLong(e.getId()),
                e.getCover(), e.getTitle(), e.getSeason())).collect(Collectors.toList());
        PageUtils<BaseAnimeDto> pageUtils = new PageUtils();
        pageUtils.setTotalCount(data.getTotal());
        pageUtils.setPageIndex(pageCnt);
        pageUtils.setPageSize(pageSize);
        pageUtils.setList(baseAnimeDtoList);
        return pageUtils;
    }

    private Map<String, String> mapper(Map<String, String> params, int pageCnt, int pageSize) {
        Map<String, String > res = new HashMap<>();
        res.put("partition", params.get("partitionId"));
        res.put("order", params.get("order"));
        res.put("season_version", params.get("seasonVersionId"));
        res.put("spoken_language_type", params.get("spokenLanguageTypeId"));
        res.put("area", params.get("regionId"));
        res.put("is_finish", params.get("isFinish").equals("true")?"1":"0");
        res.put("season_month", params.get("seasonMonth"));
        res.put("year", params.get("pubTime"));
        res.put("style_id", params.get("styleIdList"));
        res.put("page", pageCnt+"");
        res.put("page_size",pageSize+"");
        return res;
    }
}
