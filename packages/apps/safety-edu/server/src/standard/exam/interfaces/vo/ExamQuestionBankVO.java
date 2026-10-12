package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamQuestionTypeEnum;
import lombok.Data;

/**
 * 考题库返回参数
 *
 * @author lanchun
 * @date 2023/4/18 15:51
 */
@Data
public class ExamQuestionBankVO {

    private Long id;

    /**
     * 题目名称
     */
    private String name;

    /**
     * 主题主键
     */
    private Long themeId;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 题目类型
     */
    private ExamQuestionTypeEnum questionType;

    /**
     * 题目类型名称
     */
    private String questionTypeName;

    /**
     * 题目内容(表单json信息)
     */
    private String questionContent;

    /**
     * 分数
     */
    private Float score;

    /**
     * 答案解析
     */
    private String answerAnalysis;
}
