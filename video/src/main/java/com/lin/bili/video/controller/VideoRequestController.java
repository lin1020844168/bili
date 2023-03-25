package com.lin.bili.video.controller;

import com.lin.bili.video.service.VideoRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/video")
public class VideoRequestController {
    @Autowired
    private VideoRequestService videoRequestService;

    @GetMapping("/bilibili/{id}/{quality}")
    public String getVideoRequestPath(@PathVariable("id") String id, @PathVariable("quality") String quality) throws Exception {
        String url = videoRequestService.getVideoRequestUrl(id, quality);
        return "redirect:"+url;
    }

}
