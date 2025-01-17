package com.carbarn.common.toolkit.extend;

/**
 * @Description 抛出异常的Runnable
 * @Author zoulingxi
 * @Date 2023/4/4 14:07
 */
public interface ThrowableRunnable<X extends Throwable> {
    /**
     * 运行函数
     *
     * @throws X 异常
     */
    void run() throws X;
}
