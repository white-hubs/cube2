package com.guhecloud.cube.base.flow.flowable.process.interfaces;

import lombok.Getter;

public enum BacklogKey {

    /**
     * 我待办的
     */
    BACKLOG_OF_ME("WAITING"),

    /**
     * 已处理的
     */
    PROCESSED_OF_ME("PROCESSED"),

    /**
     * 我提交的
     */
    SUBMITTED_OF_ME("SUBMIT"),

    /**
     * 抄送我的
     */
    COPY_OF_ME("SERVED"),
    /**
     * 与我相关的
     */
    ALL("ALL");

    @Getter
    String value;

    BacklogKey(String value) {
        this.value = value;
    }
}
