package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration;

import lombok.Getter;

/**
 * 考试任务组卷方式：统一题目（指定试卷）或随机出题（按考题规则）
 */
@Getter
public enum ExamTaskPaperModeEnum {
    UNIFIED("统一题目"),
    RANDOM("随机出题");

    private final String description;

    ExamTaskPaperModeEnum(String description) {
        this.description = description;
    }
}
