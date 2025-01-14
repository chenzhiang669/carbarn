package com.carbarn.common.utils;

import org.slf4j.MDC;

import java.util.Optional;
import java.util.UUID;

/**
 * @Description traceId相关方法
 * @Author lxzou
 * @Date 2025/1/11 14:33
 */
public class TraceIdUtils {

    public static final String TRACE_KEY = "trace_id";
    public static final String USER_KEY = "user_id";
    public static final String CONNECT_SYMBOL = "-";

    public static String generateTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateTraceId(String context) {
        return context + CONNECT_SYMBOL + generateTraceId();
    }

    public static void genAndSetTraceId(String userId) {
        setTraceId(generateTraceId(), userId);
    }

    public static void genAndSetTraceId(String context, String userId) {
        setTraceId(generateTraceId(context), userId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_KEY);
    }

    public static void setTraceId(String traceId, String userId) {
        Optional.ofNullable(traceId).ifPresent(x -> MDC.put(TRACE_KEY, traceId));
        Optional.ofNullable(userId).ifPresent(x -> MDC.put(USER_KEY, userId));
    }

    public static void clear() {
        MDC.clear();
    }

    public static void remove() {
        MDC.remove(TRACE_KEY);
        MDC.remove(USER_KEY);
    }
}
