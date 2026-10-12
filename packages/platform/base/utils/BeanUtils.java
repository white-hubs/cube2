package com.guhecloud.platform.base.util;

import cn.hutool.core.bean.BeanUtil;

import java.util.Map;

public final class BeanUtils {
    private BeanUtils() {
        // 工具类不允许实例化
    }

    public static Map<String, Object> beanToMap(Object bean, String... properties) {
        return BeanUtil.beanToMap(bean, properties);
    }

    public static void copyProperties(Object source, Object target) {
        BeanUtil.copyProperties(source, target);
    }
}
