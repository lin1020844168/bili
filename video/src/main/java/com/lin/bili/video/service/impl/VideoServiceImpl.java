package com.lin.bili.video.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.MD5;
import com.lin.bili.video.constant.VideoConstant;
import com.lin.bili.video.exception.server.VideoFileNotFoundException;
import com.lin.bili.video.exception.server.VideoMergeTimeoutException;
import com.lin.bili.video.exception.server.VideoSliceBadException;
import com.lin.bili.video.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VideoServiceImpl implements VideoService {
    /**
     * 检查下载状态，返回下载到的分片数
     *
     * @param videoName
     * @param videoSliceSize
     * @param videoSize
     * @return
     */
    @Override
    public Map<String, Object> check(String videoName, long videoSliceSize, long videoSize) throws VideoFileNotFoundException, VideoSliceBadException {
        String dir = MD5.create().digestHex(videoName + videoSize);
        String realUrl = VideoConstant.VIDEO_TMP_URL + File.separator + dir;
        File videoFile = new File(realUrl);
        if (!videoFile.exists()) {
            throw new VideoFileNotFoundException();
        }
        List<File> videoSliceList = FileUtil.loopFiles(realUrl);
        if (isBad(videoSliceList, videoSliceSize, videoSize)) {
            FileUtil.del(realUrl);
            throw new VideoSliceBadException();
        }
        Map<String, Object> data = new HashMap<>();
        File lastVideoSlice = videoSliceList.get(videoSliceList.size() - 1);
        int cur = getCur(lastVideoSlice);
        data.put("code", cur);
        data.put("fileSliceName", lastVideoSlice.getName());
        return data;
    }

    /**
     * 上传分片
     *
     * @param videoSliceName
     * @param videoSize
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @Override
    public int upload(String videoSliceName, long videoSize, MultipartFile multipartFile) throws IOException {
        String dir = MD5.create().digestHex(getVideoName(videoSliceName) + videoSize);
        String realUrl = VideoConstant.VIDEO_TMP_URL + File.separator + dir + File.separator + videoSliceName;
        File dest = FileUtil.mkdir(realUrl);
        multipartFile.transferTo(dest);
        return getCur(dest);
    }

    /**
     * 合并分片
     *
     * @param videoSliceName
     * @param videoSliceSize
     * @param videoSize
     */
    @Override
    public void merge(String videoSliceName, long videoSliceSize, long videoSize) throws VideoSliceBadException, VideoFileNotFoundException, VideoMergeTimeoutException {
        int total = (int) Math.ceil(videoSize * 1.0 / videoSliceSize);
        String videoName = getVideoName(videoSliceName);
        String dir = MD5.create().digestHex(videoName + videoSize);
        String tmpUrl = VideoConstant.VIDEO_TMP_URL + File.separator + dir;
        //等待两秒钟完成所有分片上传
        if (!FileUtil.exist(tmpUrl)) {
            throw new VideoFileNotFoundException();
        }
        int waitSecond = 2;
        while (waitSecond > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int videoSliceCnt = FileUtil.loopFiles(tmpUrl).size();
            if (videoSliceCnt == total) {
                break;
            }
            waitSecond--;
        }
        if (waitSecond == 0) {
            FileUtil.del(tmpUrl);
            throw new VideoMergeTimeoutException();
        }

        // 分片校检
        List<File> videoSliceList = FileUtil.loopFiles(tmpUrl);
        if (isBad(videoSliceList, videoSliceSize, videoSize)) {
            FileUtil.del(tmpUrl);
            throw new VideoSliceBadException();
        }

        //多线程合并分片
        ExecutorService threadPool = null;
        RandomAccessFile channel = null;
        try {
            threadPool = Executors.newFixedThreadPool(10);
            File downloadFile = new File(VideoConstant.VIDEO_FILE_URL + File.separator + videoName);
            channel = new RandomAccessFile(downloadFile, "rw");
            int avg = total / 10;
            List<CompletableFuture<Void>> futureList = new ArrayList<>();
            for (int i = 0; i < videoSliceList.size(); i += avg) {
                int tmp = i;
                RandomAccessFile channelTmp = channel;
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    for (int j = tmp; j < j + avg && j < videoSliceList.size(); j++) {
                        File file = videoSliceList.get(j);
                        String sliceName = file.getName();
                        String realUrl = VideoConstant.VIDEO_TMP_URL + File.separator + dir + File.separator + sliceName;
                        try (FileInputStream is = new FileInputStream(realUrl)) {
                            byte[] buffer = new byte[(int) videoSliceSize];
                            int len = is.read(buffer);
                            int cur = getCur(file);
                            synchronized (this) {
                                channelTmp.seek(cur * videoSliceSize);
                                channelTmp.write(buffer, 0, len);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, threadPool);
                futureList.add(future);
            }
            CompletableFuture<Void> all = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
            all.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (threadPool != null) {
                threadPool.shutdown();
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            FileUtil.del(tmpUrl);
        }
    }

    /**
     * 判断分片是否损坏
     *
     * @param videoSliceList
     * @param videoSliceSize
     * @param videoSize
     * @return
     */
    private boolean isBad(List<File> videoSliceList, long videoSliceSize, long videoSize) {
        Collections.sort(videoSliceList, Comparator.comparingInt(a -> Integer.parseInt(a.getName().split("-")[2])));
        File lastVideoSlice = videoSliceList.get(videoSliceList.size() - 1);
        if (getCur(lastVideoSlice) != videoSliceList.size() - 1) {
            return true;
        }
        if (videoSliceList.size() * videoSliceSize >= videoSize) {
            long sum = videoSliceList.stream().mapToLong(e -> e.length()).sum();
            return sum != videoSize;
        }
        for (File file : videoSliceList) {
            if (file.length() != videoSliceSize) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取分片当前位置
     *
     * @param file
     * @return
     */
    private int getCur(File file) {
        String name = file.getName();
        String[] split = name.split("-");
        return Integer.parseInt(split[split.length - 1]);
    }

    private String getVideoName(String videoSliceName) {
        return videoSliceName.split("-")[0];
    }
}
