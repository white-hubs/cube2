package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils;

import com.guhecloud.platform.base.util.Base64Utils;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class RSAUtils {

    /**
     * 基于 原生RSA公钥加密
     *
     * @throws Exception
     */
    public static String RSAPublicKey(String password, String publicKey) {
        try {
            byte[] decoded = Base64Utils.decode(publicKey);
            RSAPublicKey pubKey =
                    (RSAPublicKey)
                            KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            // RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            //**此处Base64编码，开发者可以使用自己的库**
            return Base64Utils.encode(cipher.doFinal(password.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.error("加密失败：", e);
        }
        return "";
    }

}
