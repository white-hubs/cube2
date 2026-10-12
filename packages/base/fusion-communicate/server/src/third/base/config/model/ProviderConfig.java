package com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderConfig {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProviderConfig that = (ProviderConfig) o;
        return providerCode.equals(that.providerCode) &&
               tenant.equals(that.tenant) &&
               config.equals(that.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerCode, tenant, config);
    }

    /**
     * 厂商编号
     */
    private String providerCode;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 具体配置
     */
    private Object config;

}
