package com.lin.bili.jsuop.controller;

import com.lin.bili.jsuop.vo.IllustVo;
import com.lin.bili.jsuop.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin
public class PixivController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hot-tags")
    public String[] hotTags() {
        return restTemplate.getForObject("http://pixivapi.adicw.cn/hot-tags", String[].class);

    }

    @GetMapping("/illust/{id}")
    public IllustVo illust(@PathVariable("id") String id) {
        return restTemplate.getForObject("http://pixivapi.adicw.cn/illust/"+id, IllustVo.class);

    }

    @GetMapping("/user/{id}")
    public UserVo user(@PathVariable("id") String id) {
        return restTemplate.getForObject("http://pixivapi.adicw.cn/user/"+id, UserVo.class);
    }
}
