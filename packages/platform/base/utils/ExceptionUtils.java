package com.guhecloud.platform.base.util;

public final class ExceptionUtils {

    /**
     * 判断异常栈是否包含制定类型的异常
     *
     * @param throwable 异常栈
     * @param type      异常类型
     *
     * @return true/false
     */
    public static boolean contains(Throwable throwable, Class<? extends Throwable> type) {
        return lookup(throwable, type) != null;
    }

    /**
     * 从异常栈中查找制定类型的异常
     *
     * @param throwable 异常栈
     * @param type      异常类型
     * @param <T>       异常类型泛型
     *
     * @return 异常对象
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T lookup(Throwable throwable, Class<T> type) {
        for (; throwable != null && !type.isAssignableFrom(throwable.getClass()); throwable = throwable.getCause())
            ;
        return (T) throwable;
    }
}
