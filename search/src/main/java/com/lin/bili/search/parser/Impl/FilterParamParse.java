package com.lin.bili.search.parser.Impl;

import cn.hutool.core.date.DateUtil;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpDeserializer;
import co.elastic.clients.json.JsonpMapper;
import io.netty.util.internal.StringUtil;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lin.bili.search.constant.OrderConstant.*;

@Component
public class FilterParamParse {
    public List<Query> parse(Map<String, String> params) throws ParseException {
        List<Query> mustQueryList = new ArrayList<>();
        for (String k : params.keySet()) {
            String v = params.get(k);
            if (k.equals("order") || v.equals("-1")) {
                continue;
            }
            if (k.equals("pubTime")) {
                List<String> years = parseYear(v);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
                Query query = Query.of(q -> q.range(r -> {
                    r.field(k);
                    if (!StringUtil.isNullOrEmpty(years.get(0))) {
                        Date startYear = null;
                        try {
                            startYear = dateFormat.parse(years.get(0));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        JsonData start = JsonData.of(startYear);
                        r.gte(start);
                    }
                    if (!StringUtil.isNullOrEmpty(years.get(1))) {
                        Date endYear = null;
                        try {
                            endYear = dateFormat.parse(years.get(1));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        JsonData end = JsonData.of(endYear);
                        r.lt(end);
                    }
                    return r;
                }));
                mustQueryList.add(query);
                continue;
            }
            if (k.equals("regionId")) {
                List<String> regionIdList = Arrays.asList(v.split(","));
                Query query = Query.of(q -> q.bool(
                        b -> {
                            regionIdList.forEach(e -> b.should(s->s.term(t->t.field(k).value(e))));
                            return b;
                        }));
                mustQueryList.add(query);
                continue;
            }
            Query query = Query.of(q->q.term(t->t.field(k).value(v)));
            mustQueryList.add(query);

        }
        return mustQueryList;
    }

    private List<String> parseYear(String range) {
        int mid = 0;
        for (int i=0; i<range.length(); i++) {
            if (range.charAt(i)==',') {
                mid = i;
                break;
            }
        }
        List<String> years = new ArrayList<>();
        years.add(range.substring(1, mid));
        years.add(range.substring(mid+1, range.length()-1));
        return years;
    }

    public String parseOrder(Map<String, String> params) {
        int order = Integer.parseInt(params.get("order"));
        switch (order) {
            case FAV_CNT_ORDER:
                return "favCount";
            case HIGHEST_RANK_ORDER:
                return "rank";
            case PLAY_CNT_ORDER:
                return "playCount";
            case START_TIME_ORDER:
                return "pubTime";
            default:
                return "";
        }
    }
}
