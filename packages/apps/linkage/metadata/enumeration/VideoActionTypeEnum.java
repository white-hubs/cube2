package com.guhecloud.apps.linkage.metadata.enumeration;

/**
 * 摄像机执行类型枚举
 *
 * @author lanchun
 * @date 2022/6/21 17:08
 */
public enum VideoActionTypeEnum {
    VIDEOTAPE("录像"),
    PHOTOGRAPH("拍照");

    /**
     * 描述信息
     */
    private final String description;

    VideoActionTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
