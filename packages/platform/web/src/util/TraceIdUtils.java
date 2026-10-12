package com.guhecloud.platform.base.util;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * TraceId工具类
 */
public class TraceIdUtils {

    private static final String TRACE_ID_KEY = "traceId";

    /**
     * 生成traceId
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取当前traceId
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 设置traceId到MDC
     */
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 清除traceId
     */
    public static void clearTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }

    /**
     * 清除所有MDC
     */
    public static void clearMDC() {
        MDC.clear();
    }
}