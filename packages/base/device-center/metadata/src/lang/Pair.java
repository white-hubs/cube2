package com.guhecloud.cube.base.device.center.metadata.lang;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
}
