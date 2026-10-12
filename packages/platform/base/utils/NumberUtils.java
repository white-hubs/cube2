package com.guhecloud.platform.base.util;

public final class NumberUtils {
    private NumberUtils() {}

    public static boolean isNumber(String o) {
        return org.apache.commons.lang3.math.NumberUtils.isCreatable(o);
    }
}
