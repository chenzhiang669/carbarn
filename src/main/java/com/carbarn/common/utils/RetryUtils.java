package com.carbarn.common.utils;

import com.carbarn.common.toolkit.extend.ThrowableRunnable;
import com.carbarn.common.toolkit.extend.ThrowableSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.carbarn.common.toolkit.MoreLambda.checkAndRun;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;


/**
 * @Description 重试工具
 * @Author zoulingxi
 * @Date 2023/4/4 11:41
 */

public class RetryUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryUtils.class);

    private RetryUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 不带返回值的重试
     *
     * @param maxRetryTimes 最大重试次数
     * @param retryPeriod   重试间隔
     * @param func          执行函数
     * @param <X>           异常
     * @throws X 异常
     */
    public static <X extends Throwable> void runWithRetry(int maxRetryTimes, long retryPeriod,
                                                          ThrowableRunnable<X> func) throws X {
        supplierWithRetry(maxRetryTimes, retryPeriod, () -> {
            func.run();
            return null;
        });
    }

    /**
     * 带返回值的重试
     *
     * @param maxRetryTimes 最大重试次数
     * @param retryPeriod   重试间隔
     * @param func          执行函数
     * @param <X>           异常
     * @throws X 异常
     */
    public static <T, X extends Throwable> T supplierWithRetry(int maxRetryTimes, long retryPeriod,
                                                               ThrowableSupplier<T, X> func) throws X {
        int times = 0;
        Throwable lastException;
        do {
            try {
                return func.get();
            } catch (Throwable e) {
                if (retryPeriod >= 0) {
                    sleepUninterruptibly(retryPeriod, TimeUnit.MILLISECONDS);
                }
                times++;
                if (times <= maxRetryTimes) {
                    LOGGER.warn("retry {} times! exception : {}", times, e.toString());
                }
                lastException = e;
            }
        } while (times <= maxRetryTimes);
        throw (X) lastException;
    }

    /**
     * 对于接口不抛出异常的重试使用该方法
     *
     * @param maxRetryTimes 最大重试次数
     * @param retryPeriod   重试间隔  单位mills
     * @param func          func
     * @param checker       重试的条件
     * @param <T>           返回结果类型
     * @return 执行结果
     */
    public static <T> T supplierWithRetry(int maxRetryTimes, long retryPeriod,
                                          Supplier<T> func, Predicate<T> checker) {
        int times = 0;
        T result;
        do {
            result = func.get();
            if (!checker.test(result)) {
                break;
            }
            checkAndRun(retryPeriod >= 0, () -> sleepUninterruptibly(retryPeriod, TimeUnit.MILLISECONDS));
            times++;

            if (times < maxRetryTimes) {
                LOGGER.warn("retry {} times! invalid result : {}", times, result);
            }
        } while (times <= maxRetryTimes);
        return result;
    }

    public static <T> void runWithRetry(int maxRetryTimes, long retryPeriod,
                                        Runnable func, Predicate<T> checker) {
        supplierWithRetry(maxRetryTimes, retryPeriod, () -> {
            func.run();
            return null;
        }, checker);
    }

    /**
     * 指定间隔的重试，调用接口的话推荐逐步放大时间间隔
     *
     * @param func          func
     * @param checker       重试条件
     * @param retryUnit     重试间隔单位
     * @param retryDuration 重试间隔
     * @param <T>           返回结果类型
     * @return 函数执行结果
     */
    public static <T> T supplierWithRetry(Supplier<T> func, Predicate<T> checker,
                                          TimeUnit retryUnit, long... retryDuration) {
        int maxRetryTimes = retryDuration.length;
        int times = 0;
        T result;
        do {
            result = func.get();
            if (!checker.test(result)) {
                break;
            }
            if (times < maxRetryTimes) {
                sleepUninterruptibly(retryDuration[times], retryUnit);
            }
            times++;

            if (times <= maxRetryTimes) {
                LOGGER.warn("retry {} times! invalid result : {}", times, result);
            }
        } while (times <= maxRetryTimes);
        return result;
    }

    public static <T> T supplierWithRetry(Supplier<T> func, Predicate<T> checker, long... retryDuration) {
        return supplierWithRetry(func, checker, TimeUnit.MILLISECONDS, retryDuration);
    }

    public static <T> void runWithRetry(Runnable runnable, Predicate<T> checker,
                                        TimeUnit retryUnit, long... retryDuration) {
        supplierWithRetry(() -> {
            runnable.run();
            return null;
        }, checker, retryUnit, retryDuration);
    }

    public static <T> void runWithRetry(Runnable runnable, Predicate<T> checker, long... retryDuration) {
        runWithRetry(runnable, checker, TimeUnit.MILLISECONDS, retryDuration);
    }
}