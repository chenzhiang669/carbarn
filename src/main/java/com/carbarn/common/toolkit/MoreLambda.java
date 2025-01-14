package com.carbarn.common.toolkit;

import com.carbarn.common.toolkit.extend.ThrowableRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @Description lambda简单增强
 * @Author lxzou
 * @Date 2025/1/11 15:35
 */
public class MoreLambda {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoreLambda.class);
    private static final String MARKER = "[fail safe]";

    public static <E extends Throwable> void checkAndRun(boolean check, ThrowableRunnable<E> r) {
        if (check) {
            catchingRunning(r);
        }
    }

    public static <E extends Throwable> void checkAndRun(boolean check,
                                                         ThrowableRunnable<E> r1, ThrowableRunnable<E> r2) {
        if (check) {
            catchingRunning(r1);
        } else {
            catchingRunning(r2);
        }
    }

    public static void checkAndThrowing(boolean check, RuntimeException r) {
        if (check) {
            throw r;
        }
    }

    public static void checkAndThrowing(boolean check, Supplier<RuntimeException> r) {
        if (check) {
            throw r.get();
        }
    }

    public static void checkAndThrowing(boolean check, Runnable runnable, RuntimeException r) {
        if (check) {
            runnable.run();
            throw r;
        }
    }

    public static <T> T checkAndCall(boolean check, Callable<T> c) {
        if (check) {
            return catchingCalling(c);
        }
        return null;
    }

    public static <T> T checkAndCall(boolean check, Callable<T> c1, Callable<T> c2) {
        if (check) {
            return catchingCalling(c1);
        } else {
            return catchingCalling(c2);
        }
    }

    public static <E extends Throwable> void catchingRunning(ThrowableRunnable<E> r) {
        catchingRunning(r, MoreLambda::record);
    }

    public static <E extends Throwable> void catchingRunning(ThrowableRunnable<E> r, Consumer<Throwable> handler) {
        try {
            r.run();
        } catch (Throwable t) {
            handler.accept(t);
        }
    }

    public static @Nullable <E> E catchingCalling(Callable<E> callable) {
        return catchingCalling(callable, MoreLambda::record);
    }

    public static <E> E catchingCalling(Callable<E> callable, Consumer<Throwable> handler) {
        return catchingCalling(callable, handler, null);
    }

    public static <E> E catchingCalling(Callable<E> callable, Consumer<Throwable> handler, E defaultVal) {
        try {
            return callable.call();
        } catch (Throwable t) {
            handler.accept(t);
            return defaultVal;
        }
    }

    public static void record(Throwable t) {
        LOGGER.error(MARKER, t);
    }
}
