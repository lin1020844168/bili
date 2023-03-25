package com.lin.bili.search.exception.server;

import com.lin.bili.common.exception.ErrorServerException;

public class NotSuchStrategyException extends ErrorServerException {
    public NotSuchStrategyException() {
        super("找不到该排序规则");
    }
}
