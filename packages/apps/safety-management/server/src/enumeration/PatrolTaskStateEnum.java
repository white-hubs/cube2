package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import lombok.Getter;

/**
 * @author wql
 * @describe 电子巡更任务状态枚举
 * @date 2023/9/11 16:09
 */
@Getter
public enum PatrolTaskStateEnum {
    IN_EXECUTION("执行中"),
    TO_BE_RECEIVED("待接收"),
    FINISHED("已结束");
    private final String remarks;

    PatrolTaskStateEnum(String remarks) {
        this.remarks = remarks;
    }
}
