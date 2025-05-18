package com.carbarn.common.exception.enums;

/**
 * @Description 错误码
 * @Author lxzou
 * @Date 2025/1/11 13:13
 */
public enum ErrorCode {
    SUCCESS(200, "成功"),

    //  客户端错误段
    BAD_REQUEST(400, "请求参数不正确"),
    UNAUTHORIZED(401, "账号未登录"),
    UNEXIST(402, "账号不存在"),
    ERROR_PASSWORD(403, "密码错误"),
    ERROR_OLD_PASSWORD(408, "原密码错误"),
    NOT_FOUND(404, "请求未找到"),
    FORBIDDEN(405, "没有该操作权限"),
    METHOD_NOT_ALLOWED(406, "请求方法不正确"),
    USER_EXISTS_ERROR(407, "账号已存在"),
    FAIL_AUTH(409, "认证失败"),
    LOCKED(423, "请求失败，请稍后重试"),
    TOO_MANY_REQUESTS(429, "请求过于频繁，请稍后重试"),
    UNKNOWN(999, "未知错误");


    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误提示
     */
    private final String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
