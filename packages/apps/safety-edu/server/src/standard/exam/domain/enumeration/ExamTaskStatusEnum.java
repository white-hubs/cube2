package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration;

import lombok.Getter;

/**
 * 考试任务状态枚举
 *
 * @author lanchun
 * @date 2023/4/19 14:08
 */
@Getter
public enum ExamTaskStatusEnum {
    UNPUBLISHED("待发布"),
    STAY_EXAM("待考试"),
    IN_PROGRESS("进行中"),
    ENDED("已结束");

    /**
     * 描述信息
     */
    private final String description;

    ExamTaskStatusEnum(String description) {
        this.description = description;
    }
}
