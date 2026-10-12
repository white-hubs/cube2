package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ObjectIdUtilsTests {
    @Test
    public void next() {
        String id1 = ObjectIdUtils.next();
        String id2 = ObjectIdUtils.next();
        assertEquals(24, id1.length());
        assertEquals(24, id2.length());
        // 生成的ID应该不相等
        assertNotEquals(id1, id2);
    }
}
