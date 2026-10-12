package com.guhecloud.platform.security.signature;

import java.util.Map;

/**
 * 数字签名处理器接口
 *
 * @author Woody
 * @date 2021/8/30
 */
public interface Signer {
    /**
     * 数字签名
     *
     * @param parameters 签名参数
     *
     * @return 签名信息
     */
    String sign(Map<?, ?> parameters);

    /**
     * 数字签名
     *
     * @param parameters 签名参数
     * @param expiration 过期时间（秒）
     *
     * @return 签名信息
     */
    String sign(Map<?, ?> parameters, int expiration);

    /**
     * 验证签名信息
     *
     * @param signature  签名信息
     * @param parameters 签名参数
     *
     * @return true/false
     */
    boolean validate(String signature, Map<?, ?> parameters);
}
