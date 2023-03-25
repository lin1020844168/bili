package com.lin.bili.common.utils;

/**
 * 常用API响应码
 * 信息响应(100–199)
 * 成功响应(200–299)
 * 重定向(300–399)
 * 客户端错误(400–499)
 * 服务器错误 (500–599)
 * 其他
 */
public enum HttpCode{

    //==========================|| 服务器响应码 ||===================================
    SUCCESS(200,"业务正确执行"),
    IRREGULAR_ACCESS(400,"客户端出错啦"),
    FAILURE(500,"服务器出错了"),
    ;
    private int code;
    private String message;

    private HttpCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 如果业务正常执行，则会返回SUCCESS
     * 所以correctExecBusiness是判断是否返回SUCCESS
     * 如果是，则返回true，否则返回false
     * @return
     */
    public boolean isSuccess(){
        if(HttpCode.SUCCESS.equals(this)){
            return true;
        }
        return false;
    }
}
