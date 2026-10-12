package com.guhecloud.cube.base.message2.server.handler.domain.wecom;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;

import java.util.Objects;

@Data
public class WeComAccountConfig {

    /**
     * 企业id
     */
    private String corpId;

    /**
     * 应用的agentId
     */
    private String appAgentId;

    /**
     * 应用访问密钥
     */
    private String appSecret;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        WeComAccountConfig that = (WeComAccountConfig) obj;
        return this.corpId.equals(that.getCorpId()) && this.appSecret.equals(that.getCorpId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(corpId, appSecret);
    }

    public static WeComAccountConfig getWeComConfig(String config) {
        return JsonUtils.deserialize(config, WeComAccountConfig.class);
    }
}
