package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration;

import lombok.Getter;

/**
 * 试卷来源：手动维护或考试任务随机生成
 */
@Getter
public enum ExamPaperSourceEnum {
    MANUAL("手动组卷"),
    EXAM_TASK_RANDOM("考试任务随机组卷");

    private final String description;

    ExamPaperSourceEnum(String description) {
        this.description = description;
    }
}
