package com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;

/**
 * 安全台账人员状态。
 */
public enum PersonStatusEnum {

    /**
     * 在职，可与 OUAA 启用状态联动恢复。
     */
    ACTIVE("在职"),

    /**
     * 禁用，OUAA 用户禁用时联动。
     */
    DISABLED("禁用"),

    /**
     * 离职，人事离职消息或 OUAA 离职联动。
     */
    LEFT("离职");

    private final String description;

    PersonStatusEnum(String description) {
        this.description = description;
    }

    public String getCode() {
        return name();
    }

    public String getDescription() {
        return description;
    }

    public static String normalize(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        boolean supported = Arrays.stream(values()).anyMatch(item -> item.name().equals(normalized));
        Assert.isParameterError(!supported, "人员状态不合法");
        return normalized;
    }

    public static String getDescription(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        String normalized = code.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(item -> item.name().equals(normalized))
                .findFirst()
                .map(PersonStatusEnum::getDescription)
                .orElse(code);
    }
}
