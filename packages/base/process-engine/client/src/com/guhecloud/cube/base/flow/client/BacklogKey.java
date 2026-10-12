package com.guhecloud.cube.base.flow.client;

import lombok.Getter;

public enum BacklogKey {

    /**
     * 我待办的
     */
    BACKLOG_OF_ME("WAITING"),

    /**
     * 我提交的
     */
    SUBMITTED_OF_ME("SUBMIT"),

    /**
     * 抄送我的
     */
    COPY_OF_ME("SERVED"),

    ALL("ALL");

    @Getter
    String value;

    BacklogKey(String value) {
        this.value = value;
    }
}
