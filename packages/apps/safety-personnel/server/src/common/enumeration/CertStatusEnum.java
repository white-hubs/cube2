package com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;

/**
 * 人员证书业务状态（在职/离职/禁用联动，非到期标签）。
 */
public enum CertStatusEnum {

    /**
     * 有效，持证人正常在职且未被禁用。
     */
    VALID("有效"),

    /**
     * 持证人已离职。
     */
    LEFT("已离职"),

    /**
     * 持证人或证书被禁用。
     */
    DISABLED("已禁用");

    private final String description;

    CertStatusEnum(String description) {
        this.description = description;
    }

    public String getCode() {
        return name();
    }

    public String getDescription() {
        return description;
    }

    public static String normalizeOrDefault(String value, CertStatusEnum defaultValue) {
        return StringUtils.isBlank(value) ? defaultValue.getCode() : normalize(value);
    }

    public static String normalize(String value) {
        Assert.isParameterError(StringUtils.isBlank(value), "证书状态不能为空");
        String normalized = value.trim().toUpperCase();
        boolean supported = Arrays.stream(values()).anyMatch(item -> item.name().equals(normalized));
        Assert.isParameterError(!supported, "证书状态不合法");
        return normalized;
    }
}
