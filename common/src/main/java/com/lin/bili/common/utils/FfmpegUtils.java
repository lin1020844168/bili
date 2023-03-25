package com.lin.bili.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class FfmpegUtils {
    private static final String FFMPEG_PATH = "E:\\environment\\ffmpeg-5.1.1-essentials_build\\bin\\ffmpeg.exe";
    private static final String TMP_PATH = "D:\\ffmpeg\\temp";

    /**
     * 把mp4转成ts文件
     */
    public static void mp4convertTs(String mp4Path, String m3u8Path) throws ConvertFailException {
        String command = "ffmpeg -i " + mp4Path + " -profile:v baseline -level 3.0 -start_number 0 -hls_time 10 -hls_list_size 0 -f hls " + m3u8Path;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            dealStream(process);
            process.waitFor();
        } catch (Exception e) {
            throw new ConvertFailException();
        }
    }

    /**
     * 合并ts文件，报存到outPath文件
     *
     * @param outPath 输出目录
     * @param paths   所有要合并的ts路径
     */
    public static void convertTs(String outPath, List<String> paths) {
        String command = "ffmpeg -i \"concat:";
        for (int i = 0; i < paths.size(); i++) {
            command += paths.get(i) + "|";
        }
        command += "\" -c copy ";
        command += outPath;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            dealStream(process);
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param videoInputPath 原视频的全路径
     * @param audioInputPath 音频的全路径
     * @param videoOutPath   视频与音频结合之后的视频的路径
     * @throws Exception
     */
    public static void convert(String videoInputPath, String audioInputPath, String videoOutPath) throws ConvertFailException {
        String command = FFMPEG_PATH + " -i " + videoInputPath + " -i " + audioInputPath + " -c:v copy -c:a aac -strict experimental " +
                " -map 0:v:0 -map 1:a:0 "
                + " -y " + videoOutPath;
        try {
            Process process = Runtime.getRuntime().exec(command);
            dealStream(process);
            process.waitFor();
        } catch (Exception e) {
            throw new ConvertFailException();
        }

    }

    /**
     * 处理process输出流和错误流，防止进程阻塞
     * ps：开两条线程是为了防止单线程阻塞没办法处理另一个缓冲区
     *
     * @param process
     */
    private static void dealStream(Process process) {
        if (process == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = in.readLine()) != null) {
//                        System.out.println(line);
                        continue;
                    }
                } catch (IOException e) {
                    log.error("处理流出现错误", e);
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error("关闭流出现错误", e);
                    }
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while ((line = err.readLine()) != null) {
//                        System.out.println(line);
                        continue;
                    }
                } catch (IOException e) {
                    log.error("处理流出现错误", e);
                } finally {
                    try {
                        err.close();
                    } catch (IOException e) {
                        log.error("关闭流出现错误", e);
                    }
                }
            }
        }.start();
    }

    public static class ConvertFailException extends Exception {
        public ConvertFailException() {
            super("转换失败");
        }
    }
}
