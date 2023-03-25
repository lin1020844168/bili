package com.lin.bili.anime.controller;

import com.lin.bili.anime.service.StyleService;
import com.lin.bili.common.utils.ResponseResult;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/anime/style")
public class StyleController {
    @Autowired
    private StyleService styleService;

    @PostMapping("/add/{name}")
    public ResponseResult<Boolean> addStyle(@PathVariable("name") String name) {
        styleService.addStyle(name);
        return ResponseResult.success(true);
    }


}
