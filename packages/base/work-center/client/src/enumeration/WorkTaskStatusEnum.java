package com.guhecloud.cube.biz.work.center.client.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 工作状态枚举
 */
@Getter
public enum WorkTaskStatusEnum {
    TODO("待办"),
    DONE("已办");
    /**
     * 描述信息
     */
    private final String description;

    WorkTaskStatusEnum(@NonNull String description) {
        this.description = description;
    }
}
