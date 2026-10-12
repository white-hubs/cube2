package com.guhecloud.cube.iot.north.bridge.consumer.util;

import lombok.Data;

@Data
public class Pair<K, V> {
    public final K key;
    public final V val;

    public Pair(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }
}
