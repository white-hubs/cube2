package com.guhecloud.platform.base.util;

import cn.hutool.core.lang.UUID;

public final class IdUtils {
    private IdUtils() {
    }

    public static long next() {
        return Snowflake.getInstance().nextId();
    }

    public static String fastUUIDString() {
        return UUID.fastUUID().toString();
    }
}
