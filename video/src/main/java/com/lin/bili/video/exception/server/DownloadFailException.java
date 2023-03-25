package com.lin.bili.video.exception.server;

import com.lin.bili.common.exception.ErrorServerException;

import java.rmi.ServerException;

public class DownloadFailException extends ErrorServerException {
    public DownloadFailException() {
        super("下载失败");
    }
}
