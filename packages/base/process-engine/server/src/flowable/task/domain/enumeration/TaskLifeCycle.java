package com.guhecloud.cube.base.flow.flowable.task.domain.enumeration;

import lombok.Getter;

public enum TaskLifeCycle {
    /**
     * 激活中
     */
    ACTIVED("待处理"),
    /**
     * 已完成
     */
    COMPLEATED("已完成");

    @Getter
    String label;

    TaskLifeCycle(String label) {
        this.label = label;
    }
}
