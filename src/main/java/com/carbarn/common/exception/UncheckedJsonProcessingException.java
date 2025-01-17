package com.carbarn.common.exception;

/**
 * @Description json解析异常
 * @Author zoulingxi
 * @Date 2023/5/1 15:39
 */
public class UncheckedJsonProcessingException extends RuntimeException {
    public UncheckedJsonProcessingException(Exception e) {
        super(e);
    }
}
