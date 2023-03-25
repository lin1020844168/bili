package com.lin.bili.search.strategy;

import com.lin.bili.common.utils.PageUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public interface OrderStrategy {
    PageUtils order(Map<String, String > params, int pageCnt, int pageSize) throws IOException, ParseException;
}
