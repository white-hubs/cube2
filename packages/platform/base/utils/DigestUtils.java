package com.guhecloud.platform.base.util;

import java.io.IOException;
import java.io.InputStream;

public final class DigestUtils {
    private DigestUtils() {
    }

    public static String md5Hex(final String data) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
    }

    public static String md5Hex(final byte[] data) {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
    }

    public static String md5Hex(final InputStream in) throws IOException {
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(in);
    }

    public static String sha256Hex(final String data) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(data);
    }
}
