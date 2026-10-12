package com.guhecloud.cube.base.message2.server.handler.domain.sms;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Getter;

import java.util.Objects;

/**
 * 集时通短信接入配置
 */
@Getter
public class VComSmsAccountConfig {

    /**
     * 用户名
     */
    private final String userName;

    /**
     * 密码
     */
    private final String password;

    /**
     * 基础url
     */
    private final String baseUrl;

    public VComSmsAccountConfig() {
        this.userName = null;
        this.password = null;
        this.baseUrl = null;
    }

    public VComSmsAccountConfig(String userName, String password, String baseUrl) {
        this.userName = userName;
        this.password = password;
        this.baseUrl = baseUrl;
    }

    public static VComSmsAccountConfig getVComConfig(String config) {
        return JsonUtils.deserialize(config, VComSmsAccountConfig.class);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VComSmsAccountConfig that = (VComSmsAccountConfig) o;
        return Objects.equals(userName, that.userName) && Objects.equals(password, that.password) &&
               Objects.equals(baseUrl, that.baseUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, baseUrl);
    }
}
