package com.guhecloud.platform.base.util;

import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class AESUtils {
    /**
     * 密钥长度
     */
    public static final int KEY_SIZE = 128;

    /**
     * AES算法名称
     */
    public static final String ALGORITHM = "AES";

    /**
     * 密码处理器算法
     */
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 密钥/加解密模型/密码处理器映射
     */
    private static final ThreadLocal<Map<String, Map<Integer, Cipher>>> SECRET_MODE_CIPHER_MAPPING =
            ThreadLocal.withInitial(HashMap::new);

    static {
        Security.addProvider(new BouncyCastleProvider());
        try {
            KeyGenerator.getInstance(ALGORITHM).init(KEY_SIZE);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private AESUtils() {
    }

    /**
     * 修正密钥
     *
     * @param key 密钥标识
     *
     * @return 修正后的密钥
     */
    public static String revise(String key) {
        if (key == null) {
            return null;
        }

        // 如果小于16位则左边补0到16位
        if (key.length() < 16) {
            return StringUtils.justRight(key, 16, '0');
        }

        // 如果长度不是8的倍数则左边补0到8的倍数
        int offset = 8 - key.length() % 8;
        return offset == 8 ? key : StringUtils.justRight(key, key.length() + offset, '0');
    }

    /**
     * 获取密码处理器
     *
     * @param mode   加解密模型
     * @param secret 密钥
     * @param iv     偏移量
     *
     * @return 密码处理器实例
     */
    private static Cipher getCipher(int mode, String secret, String iv) {
        Map<String, Map<Integer, Cipher>> cache = SECRET_MODE_CIPHER_MAPPING.get();
        Map<Integer, Cipher> ciphers = cache.computeIfAbsent(secret + iv, k -> Maps.newHashMapWithExpectedSize(2));
        Cipher cipher = ciphers.get(mode);
        if (cipher == null) {
            try {
                cipher = Cipher.getInstance(CIPHER_ALGORITHM);
                Key key = new SecretKeySpec(Base64.decodeBase64(secret), ALGORITHM);
                AlgorithmParameters parameters = AlgorithmParameters.getInstance(ALGORITHM);
                parameters.init(new IvParameterSpec(Base64.decodeBase64(iv)));
                cipher.init(mode, key, parameters);
                ciphers.put(mode, cipher);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        }
        return cipher;
    }

    /**
     * AES加密
     *
     * @param original 明文
     * @param secret   密钥
     * @param iv       偏移量
     *
     * @return 密文（base64）
     */
    public static String encode(String original, String secret, String iv) {
        if (original == null) {
            return null;
        }
        try {
            return Base64.encodeBase64String(getCipher(Cipher.ENCRYPT_MODE, secret, iv).doFinal(original.getBytes()));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密
     *
     * @param ciphertext 密文（base64）
     * @param secret     密钥
     * @param iv         偏移量
     *
     * @return 明文
     */
    public static String decode(String ciphertext, String secret, String iv) {
        if (ciphertext == null) {
            return null;
        }
        try {
            return new String(getCipher(Cipher.DECRYPT_MODE, secret, iv).doFinal(Base64.decodeBase64(ciphertext)));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据清理
     */
    public static void clear() {
        SECRET_MODE_CIPHER_MAPPING.remove();
    }

    // 私钥大小128/192/256(bits)位 即：16/24/32bytes，暂时使用128，如果扩大需要更换java/jre里面的jar包
    private static final Integer PRIVATE_KEY_SIZE_BIT = 128;
    private static final Integer PRIVATE_KEY_SIZE_BYTE = 16;
    // AES密码器
    private static final ThreadLocal<Cipher> cipher = new ThreadLocal<>();

    // 字符串编码
    private static final String KEY_CHARSET = "UTF-8";

    // 算法方式
    private static final String KEY_ALGORITHM = "AES";

    // 算法/模式/填充
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";

    /**
     * 加密
     *
     * @param secretKey 密钥：加密的规则 16位
     * @param plainText 明文：要加密的内容
     *
     * @return cipherText
     * 密文：加密后的内容，如有异常返回空串：""
     */
    public static String encrypt(String secretKey, String plainText) {
        if (secretKey.length() != PRIVATE_KEY_SIZE_BYTE) {
            throw new RuntimeException("AESUtil:Invalid AES secretKey length (must be 16 bytes)");
        }

        // 密文字符串
        String cipherText = "";
        try {
            // 加密模式初始化参数
            initParam(secretKey, Cipher.ENCRYPT_MODE);
            // 获取加密内容的字节数组
            byte[] bytePlainText = plainText.getBytes(KEY_CHARSET);
            // 执行加密
            byte[] byteCipherText = cipher.get().doFinal(bytePlainText);
            cipherText = Base64.encodeBase64String(byteCipherText);
        } catch (Exception e) {
            throw new RuntimeException("AESUtil:encrypt fail!", e);
        } finally {
            if (Objects.nonNull(cipher.get())) {
                cipher.remove();
            }
        }
        return cipherText;
    }

    /**
     * 解密
     *
     * @param secretKey  密钥：加密的规则 16位
     * @param cipherText 密文：加密后的内容，即需要解密的内容
     *
     * @return plainText
     * 明文：解密后的内容即加密前的内容，如有异常返回空串：""
     */
    public static String decrypt(String secretKey, String cipherText) {
        if (secretKey.length() != PRIVATE_KEY_SIZE_BYTE) {
            throw new RuntimeException("AESUtil:Invalid AES secretKey length (must be 16 bytes)");
        }

        // 明文字符串
        String plainText = "";
        try {
            initParam(secretKey, Cipher.DECRYPT_MODE);
            // 将加密并编码后的内容解码成字节数组
            byte[] byteCipherText = Base64.decodeBase64(cipherText);
            // 解密
            byte[] bytePlainText = cipher.get().doFinal(byteCipherText);
            plainText = new String(bytePlainText, KEY_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("AESUtil:decrypt fail!", e);
        } finally {
            if (Objects.nonNull(cipher.get())) {
                cipher.remove();
            }
        }
        return plainText;
    }

    /**
     * 初始化参数
     *
     * @param secretKey 密钥：加密的规则 16位
     * @param mode      加密模式：加密or解密
     */
    private static void initParam(String secretKey, int mode) {
        try {
            // 防止Linux下生成随机key
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(secretKey.getBytes());
            // 获取key生成器
            KeyGenerator keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
            keygen.init(PRIVATE_KEY_SIZE_BIT, secureRandom);

            // 获得原始对称密钥的字节数组
            byte[] raw = secretKey.getBytes();

            // 根据字节数组生成AES内部密钥
            SecretKeySpec key = new SecretKeySpec(raw, KEY_ALGORITHM);
            // 根据指定算法"AES/CBC/PKCS5Padding"实例化密码器
            cipher.set(Cipher.getInstance(CIPHER_ALGORITHM_CBC));
            IvParameterSpec iv = new IvParameterSpec(secretKey.getBytes());
            cipher.get().init(mode, key, iv);
        } catch (Exception e) {
            throw new RuntimeException("AESUtil:initParam fail!", e);
        }
    }

}
