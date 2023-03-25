package com.lin.bili.video.exception.server;

import com.lin.bili.common.exception.WarnServerException;

public class VideoSliceBadException extends WarnServerException {
    public VideoSliceBadException() {
        super("当前视频分片已损坏");
    }
}
