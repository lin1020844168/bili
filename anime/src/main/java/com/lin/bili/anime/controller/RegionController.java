package com.lin.bili.anime.controller;

import com.lin.bili.anime.service.RegionService;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/anime/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("/add/{name}")
    public ResponseResult<Boolean> addRegion(@PathVariable("name") String name) {
        regionService.addRegion(name);
        return ResponseResult.success(true);
    }
}
