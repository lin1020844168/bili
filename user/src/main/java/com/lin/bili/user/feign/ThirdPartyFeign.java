package com.lin.bili.user.feign;

import com.lin.bili.common.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 调用第三方接口
 */
@FeignClient("third-party")
public interface ThirdPartyFeign {
    @PostMapping("/sms/{phone}/{code}")
    ResponseResult sendCode(@PathVariable("phone") String phoneNumber, @PathVariable("code") String code) ;

}
