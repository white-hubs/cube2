package com.guhecloud.platform.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * author: ht
 * create: 2025-05-20 14:38
 */
public final class ListUtils {
    private ListUtils() {}

    /**
     * 非空获取当前集合，null则获取另外的集合
     *
     * @param list          需要校验的的list
     * @param otherSupplier null情况获取的supplier
     * @param <T>           集合类型
     *
     * @return 结果
     */
    public static <T> List<T> orElse(List<T> list, Supplier<List<T>> otherSupplier) {
        if (list == null || list.isEmpty()) {
            return otherSupplier.get();
        }
        return list;
    }

    /**
     * list非空转换list的元素
     *
     * @param list    需要转换的list
     * @param convert 转换器
     * @param <T>     转换前元素类型
     * @param <R>     转换后元素类型
     *
     * @return 转换结果
     */
    public static <T, R> List<R> convertItem(List<T> list, Function<T, R> convert) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().filter(Objects::nonNull).map(convert).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     *
     * @return true/false
     */
    public static boolean nonEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * 按照规则过滤元素
     *
     * @param list      过滤的集合
     * @param predicate 过滤器
     * @param <T>       元素类型
     *
     * @return 过滤后的集合
     */
    public static <T> List<T> removeItem(List<T> list, Predicate<T> predicate) {
        if (ListUtils.nonEmpty(list)) {
            return list.stream().filter(predicate).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 删除指定字段为null的空元素
     *
     * @param list   删除的集合
     * @param mapper 映射字段
     * @param <T>    元素类型
     * @param <R>    返回类型
     *
     * @return 删除后的集合
     */
    public static <T, R> List<T> removeIfNull(List<T> list, Function<T, R> mapper) {
        if (ListUtils.nonEmpty(list)) {
            return list.stream().filter(item -> mapper.apply(item) != null).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合
     * @param <T>        集合类型
     *
     * @return 集合是否为空
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
        return !nonEmpty(collection);
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    /**
     * 非空的时候执行，否则执行其他的
     *
     * @param list         集合
     * @param consumer     执行器
     * @param elseRunnable 其他执行器
     * @param <T>          集合类型
     */
    public static <T> void runOrElse(List<T> list, Consumer<List<T>> consumer, Runnable elseRunnable) {
        if (nonEmpty(list)) {
            consumer.accept(list);
        } else {
            elseRunnable.run();
        }
    }

    /**
     * 按长度拆分集合
     *
     * @param list 集合
     * @param size 长度
     * @param <T>  集合类型
     *
     * @return 分区集合
     */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Partition size must be positive");
        }
        if (ListUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        var total = list.size();
        var result = new ArrayList<List<T>>();
        for (var i = 0; i < total; i += size) {
            var end = Math.min(i + size, total);
            result.add(list.subList(i, end));
        }
        return result;
    }

    @SafeVarargs
    public static <T> List<T> newArrayList(T... items) {
        return List.copyOf(List.of(items));
    }

    /**
     * 去重并过滤 null，保持原有顺序。
     */
    public static <T> List<T> distinct(List<T> list) {
        if (isEmpty(list)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(new LinkedHashSet<>(removeIfNull(list, item -> item)));
    }
}
