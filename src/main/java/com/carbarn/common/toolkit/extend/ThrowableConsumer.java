package com.carbarn.common.toolkit.extend;

/**
 * @Description 抛出异常的consumer
 * @Author zoulingxi
 * @Date 2023/5/8 17:37
 */
public interface ThrowableConsumer<T, X extends Throwable> {
    /**
     * 消费函数
     *
     * @param t 消费元素
     * @throws X 抛出的异常
     */
    void accept(T t) throws X;
}
