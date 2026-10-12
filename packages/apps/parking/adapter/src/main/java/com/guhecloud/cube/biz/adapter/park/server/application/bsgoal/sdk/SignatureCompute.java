package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.sdk;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
public class SignatureCompute {

    public static String Signature(SortedMap<String, String> paramMap) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, String> stringStringEntry : paramMap.entrySet()) {
            String key = stringStringEntry.getKey();
            String value = stringStringEntry.getValue();
            list.add(key + "=" + value);
        }
        String arrString = String.join("&", list);
        log.debug("arrString:{}", arrString);
        String sign = "";
        try {
            sign = shaEncode(arrString);
        } catch (Exception e) {
            sign = "";
        }
        return sign;
    }

    public static SortedMap<String, String> sortedMap(Map<String, Object> params) {
        SortedMap<String, String> sortedParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null && !entry.getKey().equals("sign")) {
                sortedParams.put(entry.getKey().toLowerCase(), String.valueOf(entry.getValue()));
            }
        }
        return sortedParams;
    }

    private static String shaEncode(String inStr) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            log.error("shaEncode error", e);
            return "";
        }
        byte[] byteArray = inStr.getBytes(StandardCharsets.UTF_8);
        byte[] md5Bytes = sha.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            int val = ((int) md5Byte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().replace("-", "").toLowerCase();
    }
}
