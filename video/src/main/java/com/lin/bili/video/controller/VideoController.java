package com.lin.bili.video.controller;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.video.dto.EpisodeDto;
import com.lin.bili.video.dto.EpisodeOrgDto;
import com.lin.bili.video.feign.AnimeFeign;
import com.lin.bili.video.feign.JsoupFeign;
import com.lin.bili.video.mapper.VideoMapper;
import com.lin.bili.video.service.VideoService;
import org.bouncycastle.jcajce.provider.symmetric.IDEA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/video")
@RestController
public class VideoController {
    @Autowired
    private VideoService videoService;

    @Autowired
    private AnimeFeign animeFeign;

    @Autowired
    private JsoupFeign jsoupFeign;

    @PostMapping("/upload")
    public ResponseResult<Integer> upload(@RequestParam("videoSliceName") String videoSliceName,
                                 @RequestParam("videoSize") long videoSize,
                                 @RequestParam("part") MultipartFile multipartFile) throws Exception {
        int cur = videoService.upload(videoSliceName, videoSize, multipartFile);
        return ResponseResult.success(cur);
    }

    @GetMapping("/check/{videoName}/{videoSliceSize}/{videoSize}")
    public ResponseResult<Map<String, Object>> check(@PathVariable("videoName") String videoName,
                                @PathVariable("videoSliceSize") long videoSliceSize,
                                @PathVariable("videoSize") long videoSize) throws Exception {

        Map<String, Object> data = videoService.check(videoName, videoSliceSize, videoSize);
        return ResponseResult.success(data);
    }

    @PostMapping("/merge/{videoSliceName}/{videoSliceSize}/{videoSize}")
    public ResponseResult merge(@PathVariable("videoSliceName") String videoSliceName,
                                @PathVariable("videoSliceSize") long videoSliceSize,
                                @PathVariable("videoSize") long videoSize) throws Exception {
        videoService.merge(videoSliceName, videoSliceSize, videoSize);
        return ResponseResult.success();
    }

    /**
     * 获取视频分集列表
     *
     * ps:本来应该从数据库拿数据，由于目前本机没有数据，所以通过懒下载的方式从bilibili拿数据
     * @param animeId
     * @return
     */
    @GetMapping("/getEpisodeOrg/{animeId}")
    public ResponseResult<List<EpisodeOrgDto>> getEpisodeOrg(@PathVariable("animeId") String animeId) {
        EpisodeOrgDto data = jsoupFeign.getEpisodeOrg(animeId).getData();
        List<EpisodeDto> episodeDtoList =  animeFeign.allEpisode(Long.parseLong(animeId)).getData();
        List<EpisodeOrgDto.Episode> episodes = new ArrayList<>();
        episodeDtoList.forEach(e -> {
            episodes.add(new EpisodeOrgDto.Episode(e.getId().toString(), e.getTitle(), e.getTitle()));
        });
        data.setEpisodes(episodes);
        List<EpisodeOrgDto> episodeOrgDtoList = new ArrayList<>();
        episodeOrgDtoList.add(data);
        return ResponseResult.success(episodeOrgDtoList);
    }


}
