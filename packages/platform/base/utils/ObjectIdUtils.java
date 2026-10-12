package com.guhecloud.platform.base.util;

import cn.hutool.core.lang.ObjectId;

public final class ObjectIdUtils {
    private ObjectIdUtils() {
        // 工具类不允许实例化
    }

    public static String next() {
        return ObjectId.next();
    }
}
