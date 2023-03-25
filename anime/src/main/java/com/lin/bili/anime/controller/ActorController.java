package com.lin.bili.anime.controller;

import com.lin.bili.anime.service.ActorService;
import com.lin.bili.common.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/anime/actor")
public class ActorController {
    @Autowired
    private ActorService actorService;

    @PostMapping("/add/{name}")
    public ResponseResult<Boolean> addActor(@PathVariable("name") String name) {
        actorService.addActor(name);
        return ResponseResult.success(true);
    }
}
