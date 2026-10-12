package com.guhecloud.apps.linkage.metadata.enumeration;

import lombok.Getter;
import lombok.NonNull;

/**
 * 执行状态枚举
 *
 * @author lanchun
 * @date 2022/6/24 19:31
 */
@Getter
public enum ExecuteStatusEnum {
    STAY_EXECUTING("待执行"),
    IN_EXECUTION("执行中"),
    SUCCESSFUL_EXECUTION("执行成功"),
    FAILED_EXECUTION("执行失败");
    /**
     * 描述信息
     */
    private final String description;

    ExecuteStatusEnum(@NonNull String description) {
        this.description = description;
    }
}
