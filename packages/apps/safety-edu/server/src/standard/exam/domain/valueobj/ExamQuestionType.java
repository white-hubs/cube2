package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 题目类型
 *
 * @author lanchun
 * @date 2023/4/18 16:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestionType {

    /**
     * 题目类型枚举
     */
    private ExamQuestionTypeEnum questionType;

    /**
     * 题目类型名称
     */
    private String questionTypeName;
}
