package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamQuestionType;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ThemeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 试卷考题信息
 *
 * @author lanchun
 * @date 2023/4/18 14:36
 */
@Data
@NoArgsConstructor
public class ExamPaperQuestionInfo {

    private Long id;

    /**
     * 题目名称
     */
    private String name;

    /**
     * 主题信息
     */
    private ThemeInfo themeInfo;

    /**
     * 题目类型
     */
    private ExamQuestionType questionType;

    /**
     * 题目内容
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

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 答题状态：true-正确，false-错误
     */
    private Boolean answerState;

    /**
     * 编辑状态：true-已做，false-未做
     */
    private Boolean editState;

    public ExamPaperQuestionInfo(Long id,
                                 String name,
                                 ThemeInfo themeInfo,
                                 ExamQuestionType questionType,
                                 String questionContent,
                                 Float score,
                                 String answerAnalysis,
                                 Integer sort
                                ) {
        this.id = id;
        this.name = name;
        this.themeInfo = themeInfo;
        this.questionType = questionType;
        this.questionContent = questionContent;
        this.score = score;
        this.answerAnalysis = answerAnalysis;
        this.sort = sort;
    }

    public void settingAnswerState(Boolean answerState) {
        this.answerState = answerState;
    }

    public void settingEditState(Boolean editState) {
        this.editState = editState;
    }

}
