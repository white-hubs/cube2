package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class CompressUtilsTests {
    @Test
    public void gzip() throws IOException {
        assertEquals(Arrays.toString(new byte[]{
                31, -117, 8, 0, 0, 0, 0, 0, 0, -1, 51, 52, 50, 6, 0, -46, 99, 72, -120, 3, 0, 0, 0
        }), Arrays.toString(CompressUtils.gzip("123")));
    }

    @Test
    public void ungzip() throws IOException {
        assertEquals("123", CompressUtils.ungzip(new byte[]{
                31, -117, 8, 0, 0, 0, 0, 0, 0, -1, 51, 52, 50, 6, 0, -46, 99, 72, -120, 3, 0, 0, 0
        }));
    }
}
