package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SecureUtilsTests {
    @Test
    public void md5() {
        assertEquals("202cb962ac59075b964b07152d234b70", SecureUtils.md5("123"));
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", SecureUtils.md5(""));
    }
}
