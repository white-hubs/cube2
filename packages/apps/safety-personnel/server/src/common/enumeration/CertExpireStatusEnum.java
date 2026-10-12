package com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration;

import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;

/**
 * 证书到期展示状态（由有效期与预警天数计算，非库字段枚举）。
 */
public enum CertExpireStatusEnum {

    /**
     * 未到期且不在预警窗口内。
     */
    NORMAL("normal", "正常"),

    /**
     * 在预警天数内即将到期。
     */
    EXPIRING("expiring", "临期"),

    /**
     * 已超过有效期结束日。
     */
    EXPIRED("expired", "已过期");

    private final String code;
    private final String description;

    CertExpireStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static String normalize(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        boolean supported = Arrays.stream(values()).anyMatch(item -> item.code.equals(normalized));
        Assert.isParameterError(!supported, "证书到期状态不合法");
        return normalized;
    }

    public static String getDescription(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        String normalized = code.trim().toLowerCase();
        return Arrays.stream(values())
                .filter(item -> item.code.equals(normalized))
                .findFirst()
                .map(CertExpireStatusEnum::getDescription)
                .orElse(code);
    }
}
