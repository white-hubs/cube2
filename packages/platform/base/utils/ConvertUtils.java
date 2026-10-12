package com.guhecloud.platform.base.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;

import java.math.BigDecimal;
import java.util.List;

public final class ConvertUtils {
    private ConvertUtils() {
    }

    public static <T> T convert(Class<T> type, Object value) {
        try {
            return Convert.convert(type, value);
        } catch (ConvertException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toList(Class<T> elementType, Object value) {
        return Convert.toList(elementType, value);
    }

    /**
     * 转换为字符串<br>
     * 如果给定的值为{@code null}，或者转换失败，返回默认值{@code null}<br>
     * 转换失败不会报错
     *
     * @param o 被转换的值
     *
     * @return 结果
     */
    public static String toStr(Object o) {
        return Convert.toStr(o);
    }

    public static Integer toInt(Object val) {
        return Convert.toInt(val);
    }

    public static Long toLong(Object val) {
        return Convert.toLong(val);
    }

    public static Double toDouble(Object val) {
        return Convert.toDouble(val);
    }

    public static Float toFloat(Object val) {
        return Convert.toFloat(val);
    }

    public static BigDecimal toBigDecimal(Object val) {
        return Convert.toBigDecimal(val);
    }
}
