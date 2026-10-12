package com.guhecloud.platform.base.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapUtils {
    /**
     * 将list转换为map
     *
     * @param list    集合
     * @param keyFunc 需要分组的字段
     * @param <T>     集合元素类型
     * @param <K>     分组的key类型
     *
     * @return 分组后的map
     */
    public static <T, K> Map<K, List<T>> listGroupToMap(List<T> list, Function<T, K> keyFunc) {
        if (ListUtils.nonEmpty(list)) {
            return list.stream().collect(Collectors.groupingBy(keyFunc));
        }
        return new HashMap<>();
    }

    /**
     * 将list转换为map
     *
     * @param list      集合
     * @param keyFunc   key 转换器
     * @param valueFunc value转换器
     * @param <T>       元素类型
     * @param <K>       key类型
     * @param <V>       value类型
     *
     * @return 转换后的map
     */
    public static <T, K, V> Map<K, V> listToMap(List<T> list, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (ListUtils.nonEmpty(list)) {
            return list.stream()
                    .filter(Objects::nonNull)
                    .filter(t -> Objects.nonNull(keyFunc.apply(t)))
                    .filter(t -> Objects.nonNull(valueFunc.apply(t)))
                    .collect(Collectors.toMap(keyFunc, valueFunc, (old, newV) -> old));
        }
        return new HashMap<>();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static <T, R> Map<R, Long> listToCountMap(List<T> list, Function<T, R> mapper) {
        if (ListUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.groupingBy(mapper, Collectors.counting()));
    }
}
