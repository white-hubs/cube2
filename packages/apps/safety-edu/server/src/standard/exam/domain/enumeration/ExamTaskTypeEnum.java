package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration;

import lombok.Getter;

/**
 * 题目类型枚举
 *
 * @author lanchun
 * @date 2023/4/18 10:52
 */
@Getter
public enum ExamTaskTypeEnum {
    STAFF_EXAM("员工考核"),
    MERCHANT_EXAM("商户考核");

    /**
     * 描述信息
     */
    private final String description;

    ExamTaskTypeEnum(String description) {
        this.description = description;
    }

}
