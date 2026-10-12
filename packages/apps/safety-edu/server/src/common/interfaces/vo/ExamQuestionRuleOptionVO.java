package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo;

import lombok.Data;

/**
 * 试卷考题规则下拉选项（仅 id、名称）
 */
@Data
public class ExamQuestionRuleOptionVO {

    private Long id;

    /**
     * 规则名称
     */
    private String ruleName;
}
