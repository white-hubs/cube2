package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.das.api.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class DasSignatureGenerator {

    /**
     * 生成签名
     *
     * @param params 参数集合
     * @param key    密钥
     *
     * @return 签名字符串
     */
    public static String generateSign(Map<String, Object> params, String key) {
        StringBuilder stringA = new StringBuilder();

        // 按照参数名ASCII码从小到大排序（字典序），忽略空值
        TreeMap<String, Object> sortedParams = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null && !entry.getKey().equals("sign")) {
                // 小驼峰命名法处理参数名
                String camelCaseKey = toCamelCase(entry.getKey());
                sortedParams.put(camelCaseKey, entry.getValue());
            }
        }

        // 构造stringA
        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            stringA.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        // 最后拼接上key得到stringSignTemp
        String stringSignTemp = stringA + "key=" + key;

        // 对stringSignTemp进行MD5运算
        return md5(stringSignTemp).toUpperCase();
    }

    /**
     * 使用MD5算法对字符串进行加密
     *
     * @param input 输入字符串
     *
     * @return 加密后的字符串
     */
    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder text = new StringBuilder(no.toString(16));
            while (text.length() < 32) {
                text.insert(0, "0");
            }
            return text.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将字符串转换为小驼峰命名
     *
     * @param s 字符串
     *
     * @return 小驼峰命名字符串
     */
    private static String toCamelCase(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length());
        boolean nextUpperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                nextUpperCase = true;
            } else if (nextUpperCase) {
                sb.append(Character.toUpperCase(c));
                nextUpperCase = false;
            } else {
                sb.append(i == 0 ? Character.toLowerCase(c) : c);
            }
        }
        return sb.toString();
    }
}
