package com.guhecloud.platform.base.util;

import org.junit.Assert;
import org.junit.Test;

public class AssertUtilsTests {
    @Test
    public void assertTrue() {
        Assert.assertThrows("1", IllegalArgumentException.class, () -> AssertUtils.assertTrue(false, "1"));
    }

    @Test
    public void assertFalse() {
        Assert.assertThrows("1", IllegalArgumentException.class, () -> AssertUtils.assertFalse(true, "1"));
    }
}
