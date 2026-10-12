package com.guhecloud.platform.base.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DesensitizeUtilsTests {

    @Test
    public void analyse() {
        // Test desensitization of a phone number
        var desensitizedPhone = DesensitizeUtils.analyse(DesensitizeUtils.DESENSITIZE_MODE_MOBILE);
        assertEquals("138****5678", desensitizedPhone.desensitize("13812345678"));
    }

    @Test
    public void desensitize() {
        assertEquals("133****1133", DesensitizeUtils.desensitize("13311331133", DesensitizeUtils.DESENSITIZE_MODE_MOBILE));
    }

    @Test
    public void desensitizeMobileAndIdCard() {
        assertEquals("138****5678", DesensitizeUtils.desensitizeMobile("13812345678"));
        assertEquals("410***********001X", DesensitizeUtils.desensitizeIdCard("41010119900101001X"));
        assertEquals("******", DesensitizeUtils.desensitizeIdCard("123456"));
    }
}
