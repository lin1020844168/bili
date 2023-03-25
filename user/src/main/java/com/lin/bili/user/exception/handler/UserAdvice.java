package com.lin.bili.user.exception.handler;

import com.lin.bili.common.exception.ClientException;
import com.lin.bili.common.exception.WarnServerException;
import com.lin.bili.common.utils.HttpCode;
import com.lin.bili.common.utils.ResponseResult;
import com.lin.bili.user.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * UserController 统一处理异常
 */
@Slf4j
@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserAdvice {
    /**
     * 处理自定义服务器异常
     * @return
     */
    @ExceptionHandler(WarnServerException.class)
    public ResponseResult<?> handleCustomServerException(WarnServerException e) {
        return ResponseResult.failure(e.getMessage());
    }

    /**
     * 处理自定义客户端异常
     * @return
     */
    @ExceptionHandler(ClientException.class)
    public ResponseResult<?> handleCustomClientException(ClientException e) {
        return ResponseResult.failure(HttpCode.IRREGULAR_ACCESS, e.getMessage());
    }


    /**
     * 处理官方注解校检异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseResult<?> handleBindException(BindingResult bindingResult) {
        FieldError error = bindingResult.getFieldError();
        String data = error.getField()+error.getDefaultMessage();
        return ResponseResult.failure(HttpCode.IRREGULAR_ACCESS, data);
    }

    /**
     * 处理未知错误
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handleException(Exception e) {
        log.error(e.toString());
        e.printStackTrace();
        return ResponseResult.failure("意料之外的错误");
    }
}
