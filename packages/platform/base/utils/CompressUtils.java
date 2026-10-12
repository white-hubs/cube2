package com.guhecloud.platform.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 压缩工具类
 */
public final class CompressUtils {

    private CompressUtils() {}

    public static byte[] gzip(String text) throws IOException {
        var out = new ByteArrayOutputStream();
        try (var gzip = new GZIPOutputStream(out)) {
            gzip.write(text.getBytes(StandardCharsets.UTF_8));
        }
        return out.toByteArray();
    }

    public static String ungzip(byte[] encoded) throws IOException {
        var out = new ByteArrayOutputStream();
        try (var gzip = new GZIPInputStream(new ByteArrayInputStream(encoded))) {
            var buffer = new byte[1024];
            int len;
            while ((len = gzip.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        }
        return out.toString(StandardCharsets.UTF_8);
    }
}
