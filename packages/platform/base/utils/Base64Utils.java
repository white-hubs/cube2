package com.guhecloud.platform.base.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Base64Utils {
    private Base64Utils() {
        // 工具类不允许实例化
    }

    public static String encode(String str) {
        return encode(str, StandardCharsets.UTF_8);
    }

    public static String encode(String str, Charset charset) {
        return encode(str.getBytes(charset));
    }

    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decode(String str) {
        return Base64.getDecoder().decode(str);
    }
}
