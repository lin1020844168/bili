package com.lin.bili.api.exception.server;

import com.lin.bili.common.exception.ErrorServerException;
import com.lin.bili.common.exception.WarnServerException;

import java.rmi.ServerException;

public class EpisodeNotfoundException extends WarnServerException {
    public EpisodeNotfoundException() {
        super("该分集未找到");
    }
}
