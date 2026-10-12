package com.guhecloud.cube.base.message2.server.handler.domain.sms;

import lombok.Data;

import java.util.Objects;

@Data
public class TencentSmsAccountConfig {

    /**
     * 凭证id
     */
    private String securityId;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 短信应用id
     */
    private String appId;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 地域信息(该参数必传)
     */
    private String region;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TencentSmsAccountConfig that = (TencentSmsAccountConfig) o;
        return Objects.equals(securityId, that.securityId) && Objects.equals(secretKey, that.secretKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(securityId, secretKey);
    }
}
