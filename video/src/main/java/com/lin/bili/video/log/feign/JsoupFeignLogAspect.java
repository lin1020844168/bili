package com.lin.bili.video.log.feign;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Slf4j
@Component
public class JsoupFeignLogAspect {
    @Pointcut("execution(* com.lin.bili.video.feign.JsoupFeign.*(..))")
    public void feign() {}

    @Before("feign()")
    public void feignBeforeInvoke(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        List<String> args = Arrays.stream(joinPoint.getArgs()).map(e->e.toString()).collect(Collectors.toList());
        log.info("jsoup的接口被调用了"+", 方法为:" + signature + ", 参数为：", args);
    }

    @After("feign()")
    public void AfterBeforeInvoke(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        List<String> args = Arrays.stream(joinPoint.getArgs()).map(e->e.toString()).collect(Collectors.toList());
        log.info("jsoup的接口调用结束"+", 方法为:" + signature + ", 参数为：", args);
    }
}
