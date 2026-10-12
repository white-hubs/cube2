package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BarcodeUtilsTests {
    @Test
    public void qrcode() {
        var underTest = BarcodeUtils.qrcode("Hello, World!");
        assertEquals(
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADIAQAAAACFI5MzAAAA5klEQVR4Xu2WQQ6FIAxE64pjcFPFm3IMVvR3BjU//u/aWTAhBPpcNC2DmD/J7oFLk0wCTSJDmoUWr1vLBcusRLDcm63J93OrQ4plEM6LIPGKrDVJy/0h63eJo9ucz60MoUsaEr+75HUyBM7YkAihd0uqQaLtJSkR9wj0IAlGOaRC6nq4hBeMFOEZdAwbRRUijlpa8s7c8ZUQocK+qW7XVoSgkLxd8NM4D6MKyTFFOFrdOaRIgTngFfoYMS3CM1gS3ndyhK/Ono4C65CYmLVFOcvPOXiVmNElY2Hf3X6d/NUkk0CTaJMPnfdKkkaEaqkAAAAASUVORK5CYII=",
                BarcodeUtils.image2base64(underTest)
                    );
        assertEquals(200, underTest.getHeight());
        assertEquals(200, underTest.getWidth());
        var underTest1 = BarcodeUtils.qrcode("Hello, World!", 300, 300);
        assertEquals(
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAAEsAQAAAABRBrPYAAABK0lEQVR4Xu3WMQ7CMAwFUDPlGD0qOWqPwNgJ0287pVhIFIma5f+hSp2XyYpb0SO5Sa68DVkKWQpZClkKWQpZyndskcikd7nqctEehQtZHfM37dN64Do3rPYbZEWsozHBxkomsr8wXBlvFtn/GOoHmkV2DrM489V+g6yESQTMbI8CWR17xg9Ex7aQpeopbBk/UNLuzro/GlkdU4i1rrOxFl/yLWQlDHV0BzNLZ9n+askK2fho2Oo5vToeZFVM46KITaoZAqfiKFkR24JNG1wvVbIK5ndkjbfI5hjs+IaQlTC/GS7ExhXujT/I6ljHZvTJRBtzjKycYWUPwZUZXSSrZWiWbfrMIqtlltEsEbstdpSsjKGOgO1eYcmq2KeQpZClkKWQpZClkKX8mD0AeCc+sOBJs6AAAAAASUVORK5CYII=",
                BarcodeUtils.image2base64(underTest1)
                    );
        assertEquals(300, underTest1.getHeight());
        assertEquals(300, underTest1.getWidth());
    }
}
