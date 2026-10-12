package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NumberUtilsTests {
    @Test
    public void isNumber() {
        assertTrue(NumberUtils.isNumber("1"));
        assertTrue(NumberUtils.isNumber("1.1"));
        assertTrue(NumberUtils.isNumber("0"));
        assertTrue(NumberUtils.isNumber("-1"));
        assertTrue(NumberUtils.isNumber("-0"));
        assertTrue(NumberUtils.isNumber("-0.11"));
        assertFalse(NumberUtils.isNumber("1-0.11"));
    }
}
