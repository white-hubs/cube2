package com.guhecloud.platform.base.util;

import org.junit.Assert;
import org.junit.Test;

public class Base64UtilsTests {
    @Test
    public void encode() {
        String str = "Hello, World!";
        String encoded = Base64Utils.encode(str);
        Assert.assertEquals("SGVsbG8sIFdvcmxkIQ==", encoded);
    }

    @Test
    public void decode() {
        String encoded = "SGVsbG8sIFdvcmxkIQ==";
        byte[] decoded = Base64Utils.decode(encoded);
        String str = new String(decoded);
        Assert.assertEquals("Hello, World!", str);
    }
}
