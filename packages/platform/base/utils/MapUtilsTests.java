package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapUtilsTests {

    @Test
    public void isEmpty() {
        assertTrue(MapUtils.isEmpty(null));
        assertTrue(MapUtils.isEmpty(Map.of()));
        assertFalse(MapUtils.isEmpty(Map.of("key", "value")));
    }

    @Test
    public void isNotEmpty() {
        assertTrue(MapUtils.isNotEmpty(Map.of("key", "value")));
        assertFalse(MapUtils.isNotEmpty(Map.of()));
        assertFalse(MapUtils.isNotEmpty(null));
    }

    @Test
    public void listGroupToMap() {
        record TestRecord(int id, String name) {}
        assertTrue(MapUtils.listGroupToMap(null, null).isEmpty());
        assertEquals("b", MapUtils.listGroupToMap(
                List.of(new TestRecord(1, "a"), new TestRecord(1, "b"), new TestRecord(2, "b")),
                TestRecord::id).get(1).getLast().name);
    }

    @Test
    public void listToMap() {
        record TestRecord(int id, String name) {}
        assertEquals("a", MapUtils.listToMap(
                List.of(new TestRecord(1, "a"), new TestRecord(2, "b")),
                (r) -> r.id,
                (r) -> r.name).get(1));
    }
}
