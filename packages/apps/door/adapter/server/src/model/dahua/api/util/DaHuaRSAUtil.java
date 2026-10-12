package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.dahua.api.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class DaHuaRSAUtil {

    private static final String RSA_ALGORITHM = "RSA";

    /**
     * 基于原生RSA公钥加密
     *
     * @param password  用户密码
     * @param publicKey 公钥
     *
     * @return
     *
     * @throws Exception
     */
    public static String encrypt(String password, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = java.util.Base64.getDecoder().decode(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr;
        byte[] inputArray = password.getBytes(StandardCharsets.UTF_8);
        int inputLength = inputArray.length;
        // 最大加密字节数，超出最大字节数需要分组加密
        int MAX_ENCRYPT_BLOCK = 117;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        outStr = java.util.Base64.getEncoder().encodeToString(resultBytes);
        return outStr;
    }

}