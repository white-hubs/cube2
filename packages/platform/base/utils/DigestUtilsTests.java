package com.guhecloud.platform.base.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DigestUtilsTests {
    @Test
    public void md5Hex() throws IOException {
        // case 1: string
        var data = "hello";
        var md5 = DigestUtils.md5Hex(data);
        Assert.assertEquals("5d41402abc4b2a76b9719d911017c592", md5);

        // case 2: file
        java.io.File tempFile = java.io.File.createTempFile("test", ".txt");
        try (java.io.FileWriter writer = new java.io.FileWriter(tempFile)) {
            writer.write("hello");
        }
        try (java.io.FileInputStream fis = new java.io.FileInputStream(tempFile)) {
            var fileMd5 = DigestUtils.md5Hex(fis);
            Assert.assertEquals("5d41402abc4b2a76b9719d911017c592", fileMd5);
        } finally {
            tempFile.delete();
        }

        // case 3: byte array
        byte[] byteArray = data.getBytes();
        var byteMd5 = DigestUtils.md5Hex(byteArray);
        Assert.assertEquals("5d41402abc4b2a76b9719d911017c592", byteMd5);
    }

    @Test
    public void sha256Hex() {
        String data = "hello";
        String sha256 = DigestUtils.sha256Hex(data);
        Assert.assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", sha256);
    }
}
