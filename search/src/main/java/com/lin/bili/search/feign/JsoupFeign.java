package com.lin.bili.search.feign;

import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.search.dto.FilterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "jsoup", path = "/bilibili")
public interface JsoupFeign {
    @GetMapping("/filter")
    ResponseResult<FilterVo> filter(@RequestParam Map<String, String> params);
}
