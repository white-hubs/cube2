package com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration;

import com.guhecloud.platform.base.util.StringUtils;

import java.util.Arrays;

/**
 * 安委会变更类型。
 */
public enum CommitteeChangeTypeEnum {

    /**
     * 创建安委会。
     */
    CREATE("创建"),

    /**
     * 编辑安委会。
     */
    UPDATE("编辑"),

    /**
     * 删除安委会。
     */
    DELETE("删除");

    private final String description;

    CommitteeChangeTypeEnum(String description) {
        this.description = description;
    }

    public String getCode() {
        return name();
    }

    public String getDescription() {
        return description;
    }

    public static CommitteeChangeTypeEnum of(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        String normalized = code.trim().toUpperCase();
        return Arrays.stream(values())
                .filter(item -> item.name().equals(normalized))
                .findFirst()
                .orElse(null);
    }

    public static String getDescription(String code) {
        CommitteeChangeTypeEnum type = of(code);
        return type == null ? code : type.getDescription();
    }
}
