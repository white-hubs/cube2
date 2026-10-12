package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdUtilsTests {

    @Test
    public void next() {
        assertEquals(19, String.valueOf(IdUtils.next()).length());
    }

    @Test
    public void fastUUIDString() {
        String uuid = IdUtils.fastUUIDString();
        assertEquals(36, uuid.length());
        assertEquals(5, uuid.split("-").length);
        assertEquals(8, uuid.split("-")[0].length());
        assertEquals(4, uuid.split("-")[1].length());
        assertEquals(4, uuid.split("-")[2].length());
        assertEquals(4, uuid.split("-")[3].length());
        assertEquals(12, uuid.split("-")[4].length());
    }
}
