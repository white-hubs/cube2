package com.guhecloud.apps.linkage.metadata.enumeration;

/**
 * 执行方式枚举
 *
 * @author lanchun
 * @date 2022/6/21 14:47
 */
public enum ExecuteModeEnum {
    AUTOMATIC("自动"),
    MANUAL("手动");
    /**
     * 描述信息
     */
    private final String description;

    ExecuteModeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
