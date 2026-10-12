package com.guhecloud.cube.iot.mgt.comm.util;

import com.guhecloud.platform.base.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class GetRequestDecode {
    public static String decodeUrlIfNeed(String s) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(s)) {
            return s;
        }

        if (s.contains("%")) {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.toString());
        } else {
            return s;
        }
    }
}
