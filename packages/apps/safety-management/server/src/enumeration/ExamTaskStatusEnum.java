package com.guhecloud.cube.srv.gen.safety.management.enumeration;

import lombok.Getter;

@Getter
public enum ExamTaskStatusEnum {
    UNPUBLISHED("待发布"),
    ENDED("已结束"),
    IN_PROGRESS("进行中");

    private final String description;

    ExamTaskStatusEnum(String description) {
        this.description = description;
    }
}
