package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums;

import lombok.Getter;
import lombok.NonNull;

/**
 * 巡检任务执行状态
 *
 * @author appet
 * @date 2021/10/16
 */
@Getter
public enum InspectRecordStatusEnum {

    /**
     * 待巡检
     */
    EXECUTING("待巡检"),

    /**
     * 已完成
     */
    OVER("已完成"),
    /**
     * 超时完成
     */
    OVERTIME("已完成"),

    /**
     * 已过期
     */
    EXPIRE("已过期"),

    /**
     * STOP
     */
    STOP("已终止"),

    WAIT_REPAIR("待修复"),
    CANCEL_REPAIR("取消修复"),
    CANCEL_INSPECT("取消巡检"),
    ;

    /**
     * 描述信息
     */
    private final String description;

    InspectRecordStatusEnum(@NonNull String description) {
        this.description = description;
    }
}
