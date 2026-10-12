package com.guhecloud.apps.linkage.metadata.enumeration;

public enum VideoStrategyTypeEnum {
    REF_VIDEO("关联的摄像机"),
    CUSTOM_VIDEO("自定义的摄像机");

    /**
     * 描述信息
     */
    private final String description;

    VideoStrategyTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
