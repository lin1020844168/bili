package com.lin.bili.api.feign;

import com.lin.bili.api.dto.*;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@FeignClient(value = "video", path = "/video")
public interface VideoFeign {
    @GetMapping("/getEpisodeOrg/{id}")
    ResponseResult<List<EpisodeOrgDto>> getEpisodeOrg(@PathVariable("id") String id);
}
