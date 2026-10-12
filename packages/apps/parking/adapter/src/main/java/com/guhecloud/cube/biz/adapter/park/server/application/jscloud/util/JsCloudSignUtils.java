package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * 捷顺开放平台 API 签名工具
 * 对接说明: 图2 - 排序并拼接后使用 MD5withRSA 签名
 */
public final class JsCloudSignUtils {

    private static final String SIGN_ALGORITHM = "MD5withRSA";

    private JsCloudSignUtils() {
    }

    /**
     * 生成签名
     * 规则: 顶层参数按 key ASCII 升序排序，排除 sign；拼装为 paramName=paramValue&...；空值转为字符串 "null"；对结果做 MD5withRSA 签名后 Base64
     *
     * @param params     请求参数（不含 sign）
     * @param privateKey Base64 编码的 RSA 私钥
     *
     * @return Base64 编码的签名
     */
    public static String sign(Map<String, String> params, String privateKey) throws Exception {
        String signContent = buildSignContent(params);
        // 调试用：打印排序后的待签名字符串，便于与文档示例对比
        System.out.println("JsCloud sign content: " + signContent);
        byte[] data = signContent.getBytes(StandardCharsets.UTF_8);
        return sign(data, privateKey);
    }

    /**
     * 对数据进行 MD5withRSA 签名
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        var pkcs8KeySpec = new java.security.spec.PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGN_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 构建待签名字符串：参数按 key 升序，paramName=paramValue 用 & 连接，排除 sign
     */
    public static String buildSignContent(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        var sorted = new TreeMap<String, String>();
        for (Map.Entry<String, String> e : params.entrySet()) {
            if ("sign".equalsIgnoreCase(e.getKey()) || "signature".equalsIgnoreCase(e.getKey())) {
                continue;
            }
            String value = e.getValue();
            // 文档要求空值转换成字符串 "null"
            sorted.put(e.getKey(), value == null ? "null" : value);
        }
        var sb = new StringBuilder();
        for (Map.Entry<String, String> e : sorted.entrySet()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(e.getKey()).append('=').append(e.getValue());
        }
        return sb.toString();
    }

}
