package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CollectionUtilsTests {
    @Test
    public void isEmpty() {
        assertTrue(CollectionUtils.isEmpty(null));
        assertTrue(CollectionUtils.isEmpty(java.util.Collections.emptyList()));
        assertTrue(!CollectionUtils.isEmpty(java.util.Collections.singletonList("test")));
    }

    @Test
    public void isNotEmpty() {
        assertTrue(!CollectionUtils.isNotEmpty(null));
        assertTrue(!CollectionUtils.isNotEmpty(java.util.Collections.emptyList()));
        assertTrue(CollectionUtils.isNotEmpty(java.util.Collections.singletonList("test")));
    }

    @Test
    public void intersection() {
        var a = java.util.Arrays.asList("a", "b", "c");
        var b = java.util.Arrays.asList("b", "c", "d");
        var intersection = CollectionUtils.intersection(a, b);
        assertTrue(intersection.size() == 2);
        assertTrue(intersection.contains("b"));
        assertTrue(intersection.contains("c"));
    }

    @Test
    public void emptyList() {
        var list = CollectionUtils.emptyList();
        assertTrue(list != null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void removeAll() {
        var collection = java.util.Arrays.asList("a", "b", "c");
        var remove = java.util.Arrays.asList("b", "c");
        var result = CollectionUtils.removeAll(collection, remove);
        assertTrue(result.size() == 1);
        assertTrue(result.contains("a"));
    }
}
