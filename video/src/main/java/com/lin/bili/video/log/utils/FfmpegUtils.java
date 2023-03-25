package com.lin.bili.video.log.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class FfmpegUtils {
    @Pointcut("execution(* com.lin.bili.common.utils.FfmpegUtils.convert(..))")
    public void convert(){}

    @Pointcut("execution(* com.lin.bili.common.utils.FfmpegUtils.mp4convertTs(..))")
    public void mp4convertTs(){}

    @Before("convert()")
    public void beforeConvert(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("开始合成mp4"+", 合成后的路径为:"+args[2]);
    }

    @After("convert()")
    public void afterConvert(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("合成mp4结束"+", 合成后的路径为:"+args[2]);
    }

    @Before("mp4convertTs()")
    public void beforeMp4convertTs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("开始mp4转成ts"+", 转成ts的路径为:"+args[1]);
    }

    @After("mp4convertTs()")
    public void afterMp4convertTs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("合成mp4结束"+", 转成ts的路径为:"+args[1]);
    }
}
