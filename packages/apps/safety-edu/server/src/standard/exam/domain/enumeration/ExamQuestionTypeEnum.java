package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration;

import lombok.Getter;

/**
 * 题目类型枚举
 *
 * @author lanchun
 * @date 2023/4/18 10:52
 */
@Getter
public enum ExamQuestionTypeEnum {
    SINGLE_CHOICE("单选题"),
    MULTIPLE_CHOICE("多选题"),
    JUDGE("判断题");

    /**
     * 描述信息
     */
    private final String description;

    ExamQuestionTypeEnum(String description) {
        this.description = description;
    }

    public static ExamQuestionTypeEnum getQuestionType(String description) {
        for (ExamQuestionTypeEnum value : values()) {
            if (value.getDescription().equals(description)) {
                return value;
            }
        }
        return null;
    }
}
