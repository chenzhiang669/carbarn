package com.carbarn.common.toolkit.extend;

/**
 * @Description 抛出异常的Supplier
 * @Author zoulingxi
 * @Date 2023/4/4 14:07
 */
public interface ThrowableSupplier<T, X extends Throwable> {
    /**
     * 获取值
     *
     * @return 获取的值
     * @throws X 异常
     */
    T get() throws X;
}
