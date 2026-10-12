package com.guhecloud.platform.base.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class CollectionUtils {
    private CollectionUtils() {}

    public static <T> boolean isEmpty(final Collection<T> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static <O> Collection<O> intersection(final Iterable<? extends O> a, final Iterable<? extends O> b) {
        return org.apache.commons.collections4.CollectionUtils.intersection(a, b);
    }

    public static <T> List<T> emptyList() {
        return new java.util.ArrayList<>();
    }

    public static <E> Collection<E> removeAll(final Collection<E> collection, final Collection<?> remove) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(remove, "remove");
        final List<E> list = new ArrayList<>();
        for (final E obj : collection) {
            if (!remove.contains(obj)) {
                list.add(obj);
            }
        }
        return list;
    }

    public static boolean containsAny(Set<Long> a, Set<Long> b) {
        return org.apache.commons.collections4.CollectionUtils.containsAny(a, b);
    }
}
