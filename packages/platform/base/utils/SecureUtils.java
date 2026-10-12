package com.guhecloud.platform.base.util;

import cn.hutool.crypto.SecureUtil;

public final class SecureUtils {
    private SecureUtils() {
        // 工具类不允许实例化
    }

    public static String md5(String str) {
        return SecureUtil.md5(str);
    }
}
