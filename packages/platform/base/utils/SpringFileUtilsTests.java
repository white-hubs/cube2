package com.guhecloud.platform.base.util;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SpringFileUtilsTests {

    @Test
    public void getFileExtension() {
        assertEquals(Optional.of("pdf"), SpringFileUtils.getFileExtension("https://example.com/doc.pdf"));
        assertEquals(Optional.of("jpg"), SpringFileUtils.getFileExtension("http://test.com/image.jpg?width=200"));
        assertEquals(Optional.empty(), SpringFileUtils.getFileExtension("https://example.com/path"));
        assertEquals(Optional.empty(), SpringFileUtils.getFileExtension("http://example/index/api/downloadFile?file_path=%2Fopt%2Fmedia%2Fwww%2Fsnap%2Frtp%2F51010700001180000001_51010700001320000002%2F2025-10-22%2Fca6f6956634e436593ee951d0cc393f6_14-43-24.jpg"));
    }
}
