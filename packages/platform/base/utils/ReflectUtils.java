package com.guhecloud.platform.base.util;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;

public final class ReflectUtils {
    private ReflectUtils() {}

    public static Field[] getFields(Class<?> beanClass) {
        return ReflectUtil.getFields(beanClass);
    }

    public static Object getFieldValue(Object obj, Field field) {
        return ReflectUtil.getFieldValue(obj, field);
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        return ReflectUtil.getFieldValue(obj, fieldName);
    }
}
