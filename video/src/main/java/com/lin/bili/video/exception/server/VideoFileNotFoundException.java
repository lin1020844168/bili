package com.lin.bili.video.exception.server;

import com.lin.bili.common.exception.ErrorServerException;

public class VideoFileNotFoundException extends ErrorServerException {
    public VideoFileNotFoundException() {
        super("没有找到视频文件");
    }
}
