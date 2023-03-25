package com.lin.bili.jsuop.exception.server;

import com.lin.bili.common.exception.WarnServerException;

public class EpisodeNotfoundException extends WarnServerException {
    public EpisodeNotfoundException() {
        super("该分集未找到");
    }
}
