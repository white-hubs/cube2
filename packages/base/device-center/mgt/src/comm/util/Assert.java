package com.guhecloud.cube.iot.mgt.comm.util;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Collection;

/**
 * 断言工具类
 *
 * @deprecated use {@link com.guhecloud.platform.base.standard.exception.Assert} instead
 **/
@Deprecated
public class Assert {

    public static void isTrue(boolean expression, String formatStr, Object... obj) {
        if (!expression) {
            throw new ServiceException(String.format(formatStr, obj));
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ServiceException(message);
        }
    }

    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new ServiceException(message);
        }
    }

    public static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new ServiceException(message);
        }
    }

    public static void isNotBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ServiceException(message);
        }
    }

    public static void isBlank(String str, String message) {
        if (StringUtils.isNotBlank(str)) {
            throw new ServiceException(message);
        }
    }

    public static void notEmpty(Collection<?> obj, String message) {
        if (obj == null || obj.isEmpty()) {
            throw new ServiceException(message);
        }
    }

}
