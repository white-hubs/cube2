package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.enumeration;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;

public enum KeyPersonStatusEnum {
    ACTIVE("在管"),
    INACTIVE("撤管");

    private final String description;

    KeyPersonStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String normalize(String value) {
        Assert.isParameterError(StringUtils.isBlank(value), "人员状态不能为空");
        String normalized = value.trim().toUpperCase();
        boolean supported = Arrays.stream(values()).anyMatch(item -> item.name().equals(normalized));
        Assert.isParameterError(!supported, "人员状态不合法");
        return normalized;
    }

    public static String normalizeOrDefault(String value, KeyPersonStatusEnum defaultValue) {
        return StringUtils.isBlank(value) ? defaultValue.name() : normalize(value);
    }

    public static String getDescription(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(item -> item.name().equals(normalized))
                .findFirst()
                .map(KeyPersonStatusEnum::getDescription)
                .orElse(value);
    }
}
