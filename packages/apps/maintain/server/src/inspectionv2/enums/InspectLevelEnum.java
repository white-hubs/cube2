package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检级别 普遍，紧急
 *
 * @author appet
 * @date 2023/04/19
 */
@Getter
public enum InspectLevelEnum {

    /**
     * 普遍
     */
    GENERAL("普通"),

    /**
     * 紧急
     */
    URGENT("紧急");

    /**
     * 描述信息
     */
    private final String description;

    InspectLevelEnum(@NonNull String description) {
        this.description = description;
    }
}
