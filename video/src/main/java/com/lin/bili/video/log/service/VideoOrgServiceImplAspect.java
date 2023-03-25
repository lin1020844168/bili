package com.lin.bili.video.log.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

@Aspect
@Slf4j
public class VideoOrgServiceImplAspect {
    @Pointcut("execution(* com.lin.bili.video.service.impl.VideoRequestServiceImpl.getAudioVideo(..))")
    public void getAudioVideo() {}

    @Pointcut("execution(* com.lin.bili.video.service.impl.VideoRequestServiceImpl.dealAudioVideo(..))")
    public void dealAudioVideo() {}

    @Before("getAudioVideo()")
    public void beforeGetAudioVideo(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("开始下载音频视频到本地临时文件" + ", id:"+args[0] + ", quality:"+args[1]);
    }

    @After("getAudioVideo()")
    public void afterGetAudioVideo(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("下载音频视频到本地临时文件结束" + ", id:"+args[0] + ", quality:"+args[1]);
    }

    @Before("dealAudioVideo()")
    public void beforeDealAudioVideo(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("开始处理临时文件转存为m3u8" + ", 保存路径为:"+args[4]);
    }

    @After("dealAudioVideo()")
    public void afterDealAudioVideo(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("处理临时文件转存为m3u8结束" + ", 保存路径为:"+args[4]);
    }
}
