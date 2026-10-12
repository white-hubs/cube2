package com.guhecloud.cube.base.message2.server.handler.domain.voice;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;

import java.util.Objects;

@Data
public class AliYunVoiceAccountConfig {

    /**
     * 阿里accessKey
     */
    private String accessKeyId;

    /**
     * 阿里accessSecret
     */
    private String accessKeySecret;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AliYunVoiceAccountConfig that = (AliYunVoiceAccountConfig) obj;
        return this.accessKeyId.equals(that.getAccessKeyId()) && this.accessKeySecret.equals(that.getAccessKeySecret());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessKeyId, accessKeySecret);
    }

    public static AliYunVoiceAccountConfig getAliConfig(String config) {
        return JsonUtils.deserialize(config, AliYunVoiceAccountConfig.class);
    }
}
