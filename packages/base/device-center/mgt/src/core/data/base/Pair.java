package com.guhecloud.cube.iot.mgt.core.data.base;

import com.google.common.base.Objects;
import lombok.Data;

@Data
public class Pair<K, V> {
    private K key;
    private V val;

    public Pair(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equal(key, pair.key) && Objects.equal(val, pair.val);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, val);
    }
}
