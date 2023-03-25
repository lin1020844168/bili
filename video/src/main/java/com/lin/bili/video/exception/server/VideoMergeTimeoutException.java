package com.lin.bili.video.exception.server;

import com.lin.bili.common.exception.WarnServerException;

public class VideoMergeTimeoutException extends WarnServerException {
    public VideoMergeTimeoutException() {
        super("合并视频超时");
    }
}
