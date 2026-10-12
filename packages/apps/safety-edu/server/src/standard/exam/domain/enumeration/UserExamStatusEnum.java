package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration;

import lombok.Getter;

/**
 * 用户考试状态枚举
 *
 * @author lanchun
 * @date 2023/4/19 14:43
 */
@Getter
public enum UserExamStatusEnum {
    STAY_EXAM(0, "待考试"),
    EXAMINED(2, "已考试"),
    MISS_EXAM(1, "缺考");

    /**
     * 描述信息
     */
    private final String description;

    private final Integer sort;

    UserExamStatusEnum(Integer sort, String description) {
        this.sort = sort;
        this.description = description;
    }
}
