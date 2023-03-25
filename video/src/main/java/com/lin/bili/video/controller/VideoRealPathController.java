package com.lin.bili.video.controller;

import com.lin.bili.video.constant.VideoConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/bilivideo")
public class VideoRealPathController {

    @GetMapping(value = "/{dir1}/{dir2}/{m3u8file:^.*\\.m3u8$}", produces = "application/vnd.apple.mpegurl")
    public byte[] parseM3u8(@PathVariable("dir1") String dir1,
                            @PathVariable("dir2") String dir2,
                            @PathVariable("m3u8file") String m3u8file) throws IOException {
        String filepath = VideoConstant.VIDEO_DIR + File.separator + dir1 + File.separator + dir2 + File.separator + m3u8file;
        FileInputStream fis = new FileInputStream(filepath);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0 ,bytes.length);
        return bytes;
    }

    @GetMapping(value = "/{dir1}/{dir2}/{tsfile:^.*\\.ts$}", produces = "application/octet-stream")
    public byte[] parseTs(@PathVariable("dir1") String dir1,
                          @PathVariable("dir2") String dir2,
                          @PathVariable("tsfile") String tsfile) throws IOException {
        String filepath = VideoConstant.VIDEO_DIR + File.separator + dir1 + File.separator + dir2 + File.separator + tsfile;
        FileInputStream fis = new FileInputStream(filepath);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0 ,bytes.length);
        return bytes;
    }
}
