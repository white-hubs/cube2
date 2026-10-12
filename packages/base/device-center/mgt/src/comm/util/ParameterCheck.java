package com.guhecloud.cube.iot.mgt.comm.util;

import com.guhecloud.platform.base.standard.exception.InvalidArgumentException;

public class ParameterCheck {
    private ParameterCheck() {
    }

    /**
     * 检查参数是否为真
     *
     * @param condition 条件
     * @param message 错误信息
     */
    public static void checkIsTrue(boolean condition, String message) {
        if (!condition) {
            throw new InvalidArgumentException(message);
        }
    }

    /**
     * 检查参数是否为真
     *
     * @param condition 条件
     * @param message 错误信息
     */
    public static void isTrue(boolean condition, String message) {
        checkIsTrue(condition, message);
    }

    /**
     * 检查参数是否为假
     *
     * @param condition 条件
     * @param message 错误信息
     */
    public static void checkIsFalse(boolean condition, String message) {
        if (condition) {
            throw new InvalidArgumentException(message);
        }
    }


    /**
     * 检查参数是否不为空
     *
     * @param obj 参数
     * @param message 错误信息
     */
    public static void checkIsNotNull(Object obj, String message) {
        if (obj == null) {
            throw new InvalidArgumentException(message);
        }
    }

    /**
     * 检查参数是否不为空
     *
     * @param obj 参数
     * @param message 错误信息
     */
    public static void notNull(Object obj, String message) {
        checkIsNotNull(obj, message);
    }

}
