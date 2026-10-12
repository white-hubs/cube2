package com.guhecloud.cube.biz.fusion.communicate.server.third.base.util;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public final class Md5Utils {

    private Md5Utils() {

    }

    public static String encryptMD5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] b = md5.digest();
            StringBuilder strMD5 = new StringBuilder(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                if ((b[i] & 0xFF) < 16) {
                    strMD5.append("0");
                }
                strMD5.append(Long.toHexString(b[i] & 0xFF));
            }
            return strMD5.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("未提供MessageDigestSpi的实现", e.getMessage());
            e.printStackTrace();
        }
        return StringUtils.EMPTY_STRING;
    }
}
