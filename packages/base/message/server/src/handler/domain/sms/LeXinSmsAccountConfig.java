package com.guhecloud.cube.base.message2.server.handler.domain.sms;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;

import java.util.Objects;

@Data
public class LeXinSmsAccountConfig {

    /**
     * 请求基地址
     */
    private String requestBaseUrl;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用密钥
     */
    private String appSecret;

    /**
     * 签名
     */
    private String signName;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LeXinSmsAccountConfig that = (LeXinSmsAccountConfig) o;
        return Objects.equals(appId, that.appId) && Objects.equals(appSecret, that.appSecret) && Objects.equals(signName, that.signName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, appSecret, signName);
    }

    public static LeXinSmsAccountConfig getLeXinConfig(String config) {
        return JsonUtils.deserialize(config, LeXinSmsAccountConfig.class);
    }
}
