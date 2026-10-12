package com.guhecloud.platform.base.util;

/**
 * 非业务异常断言工具类
 */
public final class AssertUtils {
    private AssertUtils() {}

    /**
     * 断言表达式为真
     *
     * @param expression   表达式
     * @param errorMessage 错误消息
     */
    public static void assertTrue(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * 断言表达式为假
     *
     * @param expression   表达式
     * @param errorMessage 错误消息
     */
    public static void assertFalse(boolean expression, String errorMessage) {
        if (expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void assertNotNull(Object object, String errorMessage) {
        assertTrue(object != null, errorMessage);
    }
}
