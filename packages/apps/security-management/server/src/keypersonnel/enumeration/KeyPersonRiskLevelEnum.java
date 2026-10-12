package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.enumeration;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;

public enum KeyPersonRiskLevelEnum {
    HIGH("高风险"),
    MEDIUM("中风险"),
    LOW("低风险");

    private final String description;

    KeyPersonRiskLevelEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String normalize(String value) {
        Assert.isParameterError(StringUtils.isBlank(value), "风险等级不能为空");
        String normalized = value.trim().toUpperCase();
        boolean supported = Arrays.stream(values()).anyMatch(item -> item.name().equals(normalized));
        Assert.isParameterError(!supported, "风险等级不合法");
        return normalized;
    }

    public static String getDescription(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(item -> item.name().equals(normalized))
                .findFirst()
                .map(KeyPersonRiskLevelEnum::getDescription)
                .orElse(value);
    }
}
