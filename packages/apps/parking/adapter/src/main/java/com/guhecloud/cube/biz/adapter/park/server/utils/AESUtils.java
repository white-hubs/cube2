package com.guhecloud.cube.biz.adapter.park.server.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * AES 加密类
 */
@Slf4j
public class AESUtils {
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM =
            "AES/ECB/PKCS5Padding";

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     *
     * @return 密钥
     */
    public static Key toKey(byte[] key) {
        // 生成密钥
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  密钥
     *
     * @return byte[] 加密数据
     *
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key) throws
            Exception {
        return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 加密
     *
     * @param data            待加密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     *
     * @return byte[] 加密数据
     *
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, Key key, String
                                         cipherAlgorithm
                                ) throws Exception {
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  二进制密钥
     *
     * @return byte[] 解密数据
     *
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws
            Exception {
        return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             二进制密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     *
     * @return byte[] 解密数据
     *
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, String
                                         cipherAlgorithm
                                ) throws Exception {
        // 还原密钥
        Key k = toKey(key);
        return decrypt(data, k, cipherAlgorithm);
    }

    /**
     * 解密
     *
     * @param data            待解密数据
     * @param key             密钥
     * @param cipherAlgorithm 加密算法/工作模式/填充方式
     *
     * @return byte[] 解密数据
     *
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, Key key, String
                                         cipherAlgorithm
                                ) throws Exception {
        // 实例化
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        // 使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 将 16 进制转换为二进制
     *
     * @param hexStr
     *
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2,
                                                         i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 +
                                                        1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将二进制转换成 16 进制
     *
     * @param buf
     *
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
}