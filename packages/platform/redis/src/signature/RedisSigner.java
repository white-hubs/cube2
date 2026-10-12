package com.guhecloud.platform.redis.signature;

import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.security.signature.Signer;
import lombok.NonNull;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的数字签名实现
 *
 * @author Woody
 * @date 2021/8/30
 */
public class RedisSigner implements Signer {
    @Override
    public String sign(@NonNull Map<?, ?> parameters) {
        return RedisUtils.sign(parameters);
    }

    @Override
    public String sign(Map<?, ?> parameters, int expiration) {
        return RedisUtils.sign(parameters, Expiration.from(expiration, TimeUnit.SECONDS));
    }

    @Override
    public boolean validate(@NonNull String signature, @NonNull Map<?, ?> parameters) {
        return RedisUtils.validateSignature(signature, parameters);
    }
}
