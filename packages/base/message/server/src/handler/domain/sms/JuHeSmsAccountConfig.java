package com.guhecloud.cube.base.message2.server.handler.domain.sms;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Getter;

import java.util.Objects;

@Getter
public class JuHeSmsAccountConfig {

    private String appKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JuHeSmsAccountConfig that = (JuHeSmsAccountConfig) o;
        return Objects.equals(getAppKey(), that.getAppKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAppKey());
    }

    public static JuHeSmsAccountConfig getJuHeConfig(String config) {
        return JsonUtils.deserialize(config, JuHeSmsAccountConfig.class);
    }
}
