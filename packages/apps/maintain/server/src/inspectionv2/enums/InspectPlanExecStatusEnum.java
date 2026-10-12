package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检计划执行状态
 *
 * @author appet
 * @date 2021/10/16
 */
@Getter
public enum InspectPlanExecStatusEnum {
    /**
     * 执行中
     */
    EXECUTING("执行中"),

    /**
     * OVER
     */
    OVER("已结束"),

    /**
     * STOP
     */
    STOP("已停用");

    /**
     * 描述信息
     */
    private final String description;

    InspectPlanExecStatusEnum(@NonNull String description) {
        this.description = description;
    }
}
