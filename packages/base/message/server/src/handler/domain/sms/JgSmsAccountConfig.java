package com.guhecloud.cube.base.message2.server.handler.domain.sms;

import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.Base64Utils;
import lombok.Getter;

import java.util.Objects;

/**
 * 极光通道配置数据模型
 *
 * @author lanchun
 * @date 2021/11/9 17:35
 */
@Getter
public class JgSmsAccountConfig {

    /**
     * 客户端id
     */
    private String appKey;

    /**
     * 客户端密钥
     */
    private String secret;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JgSmsAccountConfig that = (JgSmsAccountConfig) obj;
        return this.appKey.equals(that.getAppKey()) && this.secret.equals(that.getSecret());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appKey, secret);
    }

    public JgSmsAccountConfig() {
    }

    public JgSmsAccountConfig(String appKey, String secret) {
        this.appKey = appKey;
        this.secret = secret;
    }

    public static JgSmsAccountConfig getJgConfig(String config) {
        return JsonUtils.deserialize(JsonUtils.deserialize(config, Object.class), JgSmsAccountConfig.class);
    }

    public static String authorizationChecker(String config) {
        JgSmsAccountConfig jgSmsAccountConfig = JgSmsAccountConfig.getJgConfig(config);
        return Base64Utils.encode((jgSmsAccountConfig.getAppKey() + ":" + jgSmsAccountConfig.getSecret()).getBytes());
    }
}
