package com.guhecloud.platform.redis.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class RedisJwtActiveStoreProperties {

    /**
     * 是否开启跨端token校验
     */
    private final Boolean crossTokenTerminalValid = false;

    /**
     * 是否开启某端多地登录限制
     */
    private final Boolean multiSiteLoginValid = false;

    private static final String TOKEN_ACTIVE_VALIDATOR_CONFIG_KEY = "DICTIONARY:TOKEN_ACTIVE_VALIDATOR:0:JWT::ACTIVE::STORE";

    public static RedisJwtActiveStoreProperties getConfig() {
        var redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        //从redis获取store配置
        var o = redisTemplate.opsForValue().get(TOKEN_ACTIVE_VALIDATOR_CONFIG_KEY);
        if (Objects.isNull(o)) {
            return new RedisJwtActiveStoreProperties();
        }
        var variable = getStringObjectMap(o);
        var redisJwtActiveStoreProperties = JsonUtils.deserialize(variable, RedisJwtActiveStoreProperties.class);
        if (Objects.isNull(redisJwtActiveStoreProperties)) {
            redisJwtActiveStoreProperties = new RedisJwtActiveStoreProperties();
        }
        return redisJwtActiveStoreProperties;
    }

    private static String getStringObjectMap(Object value) {
        if (Objects.isNull(value)) {
            return null;
        }
        var stringObjectMap = JsonUtils.deserialize(value.toString(), new TypeReference<Map<String, Object>>() {
        });
        if (MapUtils.isEmpty(stringObjectMap) || Objects.isNull(stringObjectMap.get("variable"))) {
            return null;
        }
        return stringObjectMap.get("variable").toString();
    }
}
