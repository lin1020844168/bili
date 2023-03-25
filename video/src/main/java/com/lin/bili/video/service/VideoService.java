package com.lin.bili.video.service;

import com.lin.bili.video.exception.server.VideoFileNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface VideoService {
    Map<String, Object> check(String videoName, long videoSliceSize, long videoSize) throws Exception;
    int upload(String videoSliceName, long videoSize, MultipartFile multipartFile) throws Exception;

    void merge(String videoSliceName, long videoSliceSize, long videoSize) throws Exception;
}
