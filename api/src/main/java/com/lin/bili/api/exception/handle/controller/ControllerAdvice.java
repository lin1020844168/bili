package com.lin.bili.api.exception.handle.controller;

import com.lin.bili.common.exception.ClientException;
import com.lin.bili.common.exception.ErrorServerException;
import com.lin.bili.common.exception.WarnServerException;
import com.lin.bili.common.utils.HttpCode;
import com.lin.bili.common.utils.ResponseResult;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.lin.bili.api.controller")
public class ControllerAdvice {
    /**
     * 处理自定义服务器异常
     * @return
     */
    @ExceptionHandler(WarnServerException.class)
    public ResponseResult<String> handleCustomServerException(WarnServerException e) {
        log.warn(e.getMessage());
        return ResponseResult.failure("服务器内部错误");
    }

    /**
     * 处理自定义服务器异常
     * @return
     */
    @ExceptionHandler(ErrorServerException.class)
    public ResponseResult<String> handleCustomServerException(ErrorServerException e) {
        log.error(e.getMessage());
        return ResponseResult.failure("服务器内部错误");
    }

    /**
     * 处理自定义客户端异常
     * @return
     */
    @ExceptionHandler(ClientException.class)
    public ResponseResult<String> handleCustomClientException(ClientException e) {
        log.info(e.getMessage());
        return ResponseResult.failure(HttpCode.IRREGULAR_ACCESS, e.getMessage());
    }


    /**
     * 处理官方注解校检异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseResult<String> handleBindException(BindingResult bindingResult) {
        FieldError error = bindingResult.getFieldError();
        String data = error.getField()+error.getDefaultMessage();
        return ResponseResult.failure(HttpCode.IRREGULAR_ACCESS, data);
    }

    /**
     * 处理未知错误
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseResult.failure("意料之外的错误");
    }
}
