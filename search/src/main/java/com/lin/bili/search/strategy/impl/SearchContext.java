package com.lin.bili.search.strategy.impl;

import com.lin.bili.common.utils.PageUtils;
import com.lin.bili.search.exception.server.NotSuchStrategyException;
import com.lin.bili.search.exception.server.StrategyNotInitException;
import com.lin.bili.search.strategy.OrderStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import static com.lin.bili.search.constant.OrderConstant.*;

@Component
public class SearchContext {
    @Autowired
    private SimpleOrderStrategy simpleOrderStrategy;
    @Autowired
    private UpdateTimeOrderStrategy updateTimeOrderStrategy;

    private final OrderStrategy DEFAULT_ORDER_STRATEGY = simpleOrderStrategy;
    private OrderStrategy orderStrategy = DEFAULT_ORDER_STRATEGY;

    public void chooseStrategy(Map<String, String > params) throws NotSuchStrategyException {
        int order = Integer.parseInt(params.get("order"));
        switch (order) {
            case FAV_CNT_ORDER:
            case HIGHEST_RANK_ORDER:
            case PLAY_CNT_ORDER:
            case START_TIME_ORDER:
                setOrderStrategy(simpleOrderStrategy);
                break;
            case UPDATE_TIME_ORDER:
                setOrderStrategy(updateTimeOrderStrategy);
                break;
            default:
                throw new NotSuchStrategyException();
        }
    }

    public PageUtils search(Map<String, String> params, int pageCnt, int pageSize) throws StrategyNotInitException, IOException, ParseException {
        if (params==null) {
            throw new StrategyNotInitException();
        }
        return orderStrategy.order(params, pageCnt, pageSize);
    }

    private void setOrderStrategy(OrderStrategy orderStrategy) {
        this.orderStrategy = orderStrategy;
    }
}
