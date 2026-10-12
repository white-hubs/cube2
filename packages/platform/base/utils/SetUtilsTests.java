package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SetUtilsTests {
    @Test
    public void listToSet() {
        assertTrue(SetUtils.listToSet(List.of(1), (a) -> a) instanceof HashSet<?>);
    }
}
