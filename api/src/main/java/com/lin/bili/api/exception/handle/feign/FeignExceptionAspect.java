package com.lin.bili.api.exception.handle.feign;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * feign远程调用相关异常
 */
@Aspect
@Slf4j
@Component
public class FeignExceptionAspect {
    @Pointcut(value = "execution(* com.lin.bili.api.feign.*.*(..))")
    public void feign(){}

    @AfterThrowing(value = "feign()", throwing = "e")
    public void handleException(JoinPoint joinPoint, FeignException e) {
        Object[] args = joinPoint.getArgs();
        StringBuilder argsList = new StringBuilder("[");
        for (Object arg : args) {
            argsList.append(arg.toString()+",") ;
        }
        argsList.setCharAt(argsList.length()-1, ']');
        String message = "feign远程调用失败，调用时参数为："+argsList.toString();
        log.error(message);
    }
}
