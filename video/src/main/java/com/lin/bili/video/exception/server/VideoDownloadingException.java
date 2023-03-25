package com.lin.bili.video.exception.server;

import com.lin.bili.common.exception.ClientException;
import com.lin.bili.common.exception.WarnServerException;

public class VideoDownloadingException extends ClientException {
    public VideoDownloadingException() {
        super("视频正在下载，请稍后再试");
    }
}
