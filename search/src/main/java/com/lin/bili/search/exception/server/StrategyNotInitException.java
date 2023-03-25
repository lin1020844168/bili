package com.lin.bili.search.exception.server;

import com.lin.bili.common.exception.ErrorServerException;

public class StrategyNotInitException extends ErrorServerException {
    public StrategyNotInitException() {
        super("策略未初始化");
    }
}
