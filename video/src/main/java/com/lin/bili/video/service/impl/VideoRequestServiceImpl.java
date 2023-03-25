package com.lin.bili.video.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.lin.bili.common.constant.HttpContent;
import com.lin.bili.common.utils.FfmpegUtils;
import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.starter_redis_utils.utils.RedisUtils;
import com.lin.bili.video.constant.RedisConstant;
import com.lin.bili.video.dto.VideoAudioUrlDto;
import com.lin.bili.video.exception.server.DownloadFailException;
import com.lin.bili.video.exception.server.VideoDownloadingException;
import com.lin.bili.video.feign.JsoupFeign;
import com.lin.bili.video.mapper.VideoUrlMapper;
import com.lin.bili.video.service.VideoRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.lin.bili.video.constant.VideoConstant;

@Service
@Slf4j
public class VideoRequestServiceImpl implements VideoRequestService {
    @Autowired
    private JsoupFeign jsoupFeign;

    @Autowired
    private VideoUrlMapper videoUrlMapper;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 获取视频源请求地址
     * @param id
     * @param quality
     * @return
     */
    @Override
    public String getVideoRequestUrl(String id, String quality) throws VideoDownloadingException, DownloadFailException, FfmpegUtils.ConvertFailException {
        id = 508404+"";
        String videoRequestUrl = getFromDb(id, quality);
        if (videoRequestUrl!=null && !videoRequestUrl.isEmpty()) {
            return videoRequestUrl;
        }
        checkWithRedis(id, quality);
        String dirname = MD5.create().digestHex(id);
        String videoTempFilePath = VideoConstant.VIDEO_TEMP_DIR + File.separator + id + "-" + quality + ".mp4";
        String audioTempFIlePath = VideoConstant.VIDEO_TEMP_DIR + File.separator + id + "-" + quality + ".mp3";
        String fullVideoTempPath = VideoConstant.VIDEO_TEMP_DIR + File.separator + dirname + id + quality + ".mp4";
        String videoSaveDir = VideoConstant.VIDEO_DIR + File.separator + dirname + File.separator + id + "-" + quality;
        String videoSavePath = videoSaveDir + File.separator + "ts.m3u8";
        videoRequestUrl = VideoConstant.VIDEO_ORG_URL + "/" + dirname + "/" + id + "-" + quality + "/" + "ts.m3u8";
        try {
            getAudioVideo(id, quality, videoTempFilePath, audioTempFIlePath);
            dealAudioVideo(videoTempFilePath, audioTempFIlePath, fullVideoTempPath, videoSaveDir, videoSavePath);
        } catch (Exception e) {
            throw e;
        } finally {
            deleteTempFile(videoTempFilePath, audioTempFIlePath, fullVideoTempPath);
        }
        videoUrlMapper.insertVideoUrl(Long.parseLong(id), Integer.parseInt(quality), videoRequestUrl);
        return videoRequestUrl;
    }

    /**
     * 通过redis检测当前视频是否在下载，有效时间为30分钟
     * @param id
     * @param quality
     */
    private void checkWithRedis(String id, String quality) throws VideoDownloadingException {
        String key = MD5.create().digestHex(id + quality);
        synchronized (this) {
            if (redisUtils.hasKey(RedisConstant.VIDEO_DOWNlOAD + key)) {
                throw new VideoDownloadingException();
            }
            redisUtils.setEx(RedisConstant.VIDEO_DOWNlOAD + key, "", 60*30, TimeUnit.SECONDS);
        }
    }

    /**
     * 从数据库查询视频路径
     * @param id
     * @param quality
     * @return
     */
    private String getFromDb(String id, String quality) {
        return videoUrlMapper.getUrlByVideoIdAndQuality(Long.parseLong(id), Integer.parseInt(quality));

    }

    /**
     * 删除临时文件
     * @param videoTempFilePath
     * @param audioTempFIlePath
     * @param fullVideoTempPath
     */
    private void deleteTempFile(String videoTempFilePath, String audioTempFIlePath, String fullVideoTempPath) {
        FileUtil.del(videoTempFilePath);
        FileUtil.del(audioTempFIlePath);
        FileUtil.del(fullVideoTempPath);
    }

    /**
     * 处理音视频保存到本地
     * @param videoTempFilePath
     * @param audioTempFIlePath
     * @param fullVideoTempPath
     * @param videoSavePath
     * @param videoSaveDir
     */
    private void dealAudioVideo(String videoTempFilePath, String audioTempFIlePath, String fullVideoTempPath, String videoSaveDir, String videoSavePath) throws FfmpegUtils.ConvertFailException {
        FfmpegUtils.convert(videoTempFilePath, audioTempFIlePath, fullVideoTempPath);
        FileUtil.mkdir(videoSaveDir);
        FfmpegUtils.mp4convertTs(fullVideoTempPath, videoSavePath);
    }

    /**
     * 获取音视频临时文件
     * @param id
     * @param quality
     * @param videoTempFilePath
     * @param audioTempFIlePath
     */
    private void getAudioVideo(String id, String quality, String videoTempFilePath, String audioTempFIlePath) throws DownloadFailException {
        ResponseResult<VideoAudioUrlDto> responseResult = jsoupFeign.getEpisodeUrl(id, quality);
        VideoAudioUrlDto videoAudioUrl = responseResult.getData();
        String requestVideoUrl = videoAudioUrl.getVideoUrl();
        String requestAudioUrl = videoAudioUrl.getAudioUrl();
        writeTempFile(requestVideoUrl, videoTempFilePath);
        writeTempFile(requestAudioUrl, audioTempFIlePath);
    }

    /**
     * 通过io写入临时文件路径得到音视频临时文件
     * @param requestUrl
     * @param tempFilePath
     */
    private void writeTempFile(String requestUrl, String tempFilePath) throws DownloadFailException {
        HttpRequest request = new HttpRequest(UrlBuilder.of(requestUrl));
        initRequest(request);
        int count = 2;
        while (count>0) {
            try {
                HttpResponse response = request.executeAsync();
                response.writeBody(tempFilePath);
            } catch (Exception e) {
                count--;
                FileUtil.del(tempFilePath);
                if (count == 0) {
                    throw new DownloadFailException();
                } else {
                    continue;
                }
            }
            break;
        }
    }

    /**
     * 初始化请求头，主要是增加REFERER和COOKIE
     * @param videoRequest
     */
    private void initRequest(HttpRequest videoRequest) {
        videoRequest.header(HttpHeaders.REFERER, HttpContent.BILIBILI_REFERER);
        videoRequest.header(HttpHeaders.COOKIE, HttpContent.BILIBILI_COOKIE);
    }
}
