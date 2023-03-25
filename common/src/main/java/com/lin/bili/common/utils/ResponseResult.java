package com.lin.bili.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回对象
 */
@Data
public class ResponseResult<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(HttpCode httpCode){
        this.code = httpCode.getCode();
        this.message = httpCode.getMessage();
        this.data = null;
    }

    public ResponseResult(HttpCode httpCode, T data) {
        this.code = httpCode.getCode();
        this.message = httpCode.getMessage();
        this.data = data;
    }

    /**
     * 返回一个处理成功的响应结果
     * @return 返回一个处理成功的响应结果
     */
    public static <T> ResponseResult<T> success(){
        return new ResponseResult<T>(HttpCode.SUCCESS);
    }


    /**
     * @param httpCode 状态码
     * @return
     */
    public <T> ResponseResult<T> success(HttpCode httpCode){
        return new ResponseResult<T>(httpCode);
    }

    /**
     * 返回一个处理成功的响应结果
     * @return 返回一个处理成功的响应结果
     */
    public static <T> ResponseResult<T> success(T data){
        return new ResponseResult<T>(HttpCode.SUCCESS, data);
    }

    /**
     * 返回一个处理成功的响应结果
     * @param httpCode 状态码
     * @param data 响应的数据
     * @return 返回一个处理成功的响应结果
     */
    public static <T> ResponseResult<T> success(HttpCode httpCode, T data){
        return new ResponseResult<T>(httpCode,data);
    }

    /**
     * 返回一个处理失败的响应结果
     * @return 返回一个处理失败的响应结果
     */
    public static <T> ResponseResult<T> failure(){
        return new ResponseResult<T>(HttpCode.FAILURE);
    }


    /**
     * 返回一个处理失败的响应结果
     * @param httpCode 状态码
     * @return 返回一个处理失败的响应结果
     */
    public static <T> ResponseResult<T> failure(HttpCode httpCode){
        return new ResponseResult<T>(httpCode);
    }

    public static <T> ResponseResult<T> failure(String errMessage) {
        ResponseResult<T> failure = failure();
        failure.setMessage(failure().getMessage()+":"+errMessage);
        return failure;
    }

    public static <T> ResponseResult<T> failure(HttpCode httpCode,String errMessage) {
        ResponseResult<T> failure = failure(httpCode);
        failure.setMessage(failure.getMessage()+":"+errMessage);
        return failure;
    }

//    /**
//     * 返回一个处理失败的响应结果
//     * @param data 响应数据
//     * @return 返回一个处理失败的响应结果
//     */
//    public static <T> ResponseResult<T> failure(T data){
//        return new ResponseResult<T>(HttpCode.FAILURE, data);
//    }
//
//    /**
//     * @param httpCode 状态码
//     * @param data 响应数据
//     * @return
//     */
//    public static <T> ResponseResult<T> failure(HttpCode httpCode, T data){
//        return new ResponseResult<T>(httpCode, data);
//    }
}
