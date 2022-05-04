package com.ttpw.shorturl.exception;

import lombok.Getter;

@Getter
public enum Status {
    /**
     * 操作成功
     */
    OK(200, "操作成功"),

    BODY_NOT_MATCH(400,"请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH(401,"请求的数字签名不匹配!"),
    NOT_FOUND(404, "未找到该资源!"),
    /**
     * 未知异常
     */
    UNKNOWN_ERROR(500, "服务器出错啦"),
    SERVER_BUSY(503,"服务器正忙，请稍后再试!"),

    ;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 内容
     */
    private String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}


