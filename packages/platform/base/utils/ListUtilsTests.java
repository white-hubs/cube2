package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ListUtilsTests {
    @Test
    public void orElse() {
        assertEquals(0, ListUtils.orElse(null, Collections::emptyList).size());
        assertEquals(1, ListUtils.orElse(List.of(1), Collections::emptyList).size());
        assertEquals(1, ListUtils.orElse(Collections.emptyList(), () -> List.of(1)).size());
    }

    @Test
    public void convertItem() {
        assertEquals("1", ListUtils.convertItem(List.of(1), String::valueOf).getFirst());
    }

    @Test
    public void nonEmpty() {
        assertEquals(true, ListUtils.nonEmpty(List.of(1)));
        assertEquals(false, ListUtils.nonEmpty(Collections.emptyList()));
        assertEquals(false, ListUtils.nonEmpty(null));
    }

    @Test
    public void removeItem() {
        assertEquals("[test]", ListUtils.removeItem(List.of("test"), "test"::equals).toString());
        assertEquals("[]", ListUtils.removeItem(List.of("test"), "test1"::equals).toString());
    }

    @Test
    public void removeIfNull() {
        assertEquals("[test]", ListUtils.removeIfNull(List.of("test"), "test1"::equals).toString());
        assertEquals("[]", ListUtils.removeIfNull(List.of("test"), (item) -> null).toString());
    }

    @Test
    public void isEmpty() {
        assertFalse(ListUtils.isEmpty(List.of("test")));
        assertTrue(ListUtils.isEmpty(List.of()));
        assertTrue(ListUtils.isEmpty((List<?>) null));
    }

    @Test
    public void runOrElse() {
        var in = new AtomicInteger(0);
        ListUtils.runOrElse(List.of(1, 2, 3), (item) -> {
            in.set(item.size());
        }, () -> {});
        assertEquals(3, in.get());
    }

    @Test
    public void newArrayList() {
        assertEquals(0, ListUtils.newArrayList().size());
        assertThrows(UnsupportedOperationException.class, () -> ListUtils.newArrayList().add(1));
    }

    @Test
    public void partition() {
        List<Integer> emptyList = Collections.emptyList();
        List<List<Integer>> result = ListUtils.partition(emptyList, 3);
        assertTrue(result.isEmpty());

        List<String> list1 = Arrays.asList("A", "B");
        List<List<String>> result1 = ListUtils.partition(list1, 5);
        assertEquals(1, result1.size());
        assertEquals(Arrays.asList("A", "B"), result1.getFirst());

        List<Integer> list2 = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<List<Integer>> result2 = ListUtils.partition(list2, 2);
        assertEquals(3, result2.size());
        assertEquals(Arrays.asList(1, 2), result2.get(0));
        assertEquals(Arrays.asList(3, 4), result2.get(1));
        assertEquals(Arrays.asList(5, 6), result2.get(2));

        List<Character> list3 = Arrays.asList('a', 'b', 'c', 'd', 'e');
        List<List<Character>> result3 = ListUtils.partition(list3, 2);
        assertEquals(3, result3.size());
        assertEquals(Arrays.asList('a', 'b'), result3.get(0));
        assertEquals(Arrays.asList('c', 'd'), result3.get(1));
        assertEquals(Collections.singletonList('e'), result3.get(2));

        List<Double> list4 = Arrays.asList(1.1, 2.2, 3.3);
        List<List<Double>> result4 = ListUtils.partition(list4, 1);
        assertEquals(3, result4.size());
        assertEquals(Collections.singletonList(1.1), result4.get(0));
        assertEquals(Collections.singletonList(2.2), result4.get(1));
        assertEquals(Collections.singletonList(3.3), result4.get(2));

        List<Integer> list5 = Arrays.asList(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> ListUtils.partition(list5, 0));

        List<Integer> list6 = Arrays.asList(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> ListUtils.partition(list6, -1));
    }
}
