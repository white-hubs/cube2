package com.guhecloud.platform.base.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * author: ht
 * create: 2025-06-09 17:51
 */
public final class SetUtils {
    private SetUtils() {}

    /**
     * 将list按照转换器转换为set集合
     *
     * @param list   待转换的list
     * @param mapper 转换器
     * @param <T>    待转换的list元素类型
     * @param <R>    转换后的元素类型
     *
     * @return 转换后的set集合
     */
    public static <T, R> Set<R> listToSet(List<T> list, Function<T, R> mapper) {

        return new HashSet<>(ListUtils.convertItem(list, mapper));
    }

    public static boolean isEmpty(Set<?> set) {
        return set == null || set.isEmpty();
    }

    public static boolean isNotEmpty(Set<?> set) {
        return !isEmpty(set);
    }
}
