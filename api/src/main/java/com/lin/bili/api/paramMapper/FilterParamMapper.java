package com.lin.bili.api.paramMapper;

import java.util.HashMap;
import java.util.Map;

public class FilterParamMapper {
    // param:partition=1, page=1, order=3, season_version=-1, spoken_language_type=-1,
    // area=-1, is_finish=-1, season_month=-1, year=-1, style_id=-1
    public Map<String, String> mapper(Map<String, String> params) {
        Map<String, String> res = new HashMap<>();
        res.put("partitionId", params.get("partition"));
        res.put("order", params.get("order"));
        res.put("seasonVersionId", params.get("season_version"));
        res.put("spokenLanguageTypeId", params.get("spoken_language_type"));
        res.put("regionId", params.get("area"));
        res.put("isFinish", params.get("is_finish").equals("1")?"true":"false");
        res.put("seasonMonth", params.get("season_month"));
        res.put("pubTime", params.get("year"));
        res.put("styleIdList", params.get("style_id"));
        return res;
    }
}
