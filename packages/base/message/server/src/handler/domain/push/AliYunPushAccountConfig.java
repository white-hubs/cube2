package com.guhecloud.cube.base.message2.server.handler.domain.push;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Getter;

import java.util.Objects;

/**
 * 阿里云移动推送账号
 *
 * @author lanchun
 * @date 2023/10/25 10:03
 */
@Getter
public class AliYunPushAccountConfig {

    /**
     * 安卓应用AppKey信息
     */
    private Long androidAppKey;

    /**
     * IOS应用AppKey信息
     */
    private Long iosAppKey;

    /**
     * 阿里accessKey
     */
    private String accessKeyId;

    /**
     * 阿里accessSecret
     */
    private String accessKeySecret;

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
        AliYunPushAccountConfig that = (AliYunPushAccountConfig) obj;
        return this.accessKeyId.equals(that.getAccessKeyId()) &&
               this.accessKeySecret.equals(that.getAccessKeySecret());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessKeyId, accessKeySecret);
    }

    public static AliYunPushAccountConfig getAliConfig(String config) {
        AliYunPushAccountConfig aliVoiceConfig = JsonUtils.deserialize(config, AliYunPushAccountConfig.class);
        return aliVoiceConfig;
    }
}
