package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PinYinUtilsTests {
    @Test
    public void getFull() {
        assertEquals("nihaoa", PinYinUtils.getFull("你好a"));
    }

    @Test
    public void getHead() {
        assertEquals("nh", PinYinUtils.getHead("你好"));
        assertEquals("n", PinYinUtils.getHead("你"));
        assertEquals("h", PinYinUtils.getHead("好"));
        assertEquals("a", PinYinUtils.getHead("啊"));
        assertEquals("b", PinYinUtils.getHead("b"));
        assertEquals("1", PinYinUtils.getHead("1"));
        assertEquals("!", PinYinUtils.getHead("!"));
    }
}
