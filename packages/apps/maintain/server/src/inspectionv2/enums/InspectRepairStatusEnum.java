package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检修复状态
 *
 */
@Getter
public enum InspectRepairStatusEnum {

    WAIT_REPAIR("待修复"),

    CANCEL_REPAIR("取消修复"),

    REPAIRED("已修复"),
    ;

    /**
     * 描述信息
     */
    private final String description;

    InspectRepairStatusEnum(@NonNull String description) {
        this.description = description;
    }
}
