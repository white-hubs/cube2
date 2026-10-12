package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class AESUtilsTests {
    @Test
    public void revise() {
        assertNull(AESUtils.revise(null));
        assertEquals("0000000000000000", AESUtils.revise(""));
        assertEquals("1230000000000000", AESUtils.revise("123"));
        assertEquals("123456789012345678900000", AESUtils.revise("1234567890123456789"));
        assertEquals(24, AESUtils.revise("1234567890123456789").length());
    }

    @Test
    public void encode() {
        byte[] ivBytes = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };
        var value = "original";
        var secure = Base64.getEncoder()
                .encodeToString(AESUtils.revise("secure").getBytes());
        var iv = Base64
                .getEncoder()
                .encodeToString(ivBytes);
        var encrypted = AESUtils.encode(value, secure, iv);
        assertEquals("zWu20c0QHX8HFn4Fc9p2QQ==", encrypted);
        assertEquals(value, AESUtils.decode(encrypted, secure, iv));
        assertNull(AESUtils.encode(null, secure, iv));
        assertThrows(RuntimeException.class, () -> AESUtils.encode("", "", ""));
    }

    @Test
    public void decode() {
        assertNull(AESUtils.decode(null, "", ""));
        assertThrows(RuntimeException.class, () -> AESUtils.decode("", "", ""));
    }

    @Test
    public void encrypt() {
        assertEquals("m4R0rUAW4REnW0XPhHfDCw==", AESUtils.encrypt("1234567890123456", "1"));
    }

    @Test
    public void decrypt() {
        assertEquals("1", AESUtils.decrypt("1234567890123456", "m4R0rUAW4REnW0XPhHfDCw=="));
    }
}
