package com.guhecloud.cube.base.message2.server.handler.domain.push;

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
public class JgPushAccountConfig {

    /**
     * 客户端id
     */
    private String appKey;

    /**
     * 客户端密钥
     */
    private String secret;

    /**
     * deeplink跳转信息
     */
    private String deepLinkSchema;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JgPushAccountConfig that = (JgPushAccountConfig) obj;
        return this.appKey.equals(that.getAppKey()) && this.secret.equals(that.getSecret());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appKey, secret);
    }

    public JgPushAccountConfig() {
    }

    public JgPushAccountConfig(String appKey, String secret) {
        this.appKey = appKey;
        this.secret = secret;
    }

    public static JgPushAccountConfig getJgConfig(String config) {
        JgPushAccountConfig jgSmsAccountConfig = JsonUtils.deserialize(JsonUtils.deserialize(config, Object.class), JgPushAccountConfig.class);
        return jgSmsAccountConfig;
    }

    public static String authorizationChecker(String config) {
        JgPushAccountConfig jgSmsAccountConfig = JgPushAccountConfig.getJgConfig(config);
        return Base64Utils.encode((jgSmsAccountConfig.getAppKey() + ":" +
                                   jgSmsAccountConfig.getSecret()).getBytes());
    }
}
