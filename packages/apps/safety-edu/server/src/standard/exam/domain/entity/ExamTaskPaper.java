package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamTaskPaper {

    /**
     * 试卷主键
     */
    private Long id;

    /**
     * 随机组卷规则主键
     */
    private Long ruleId;

    /**
     * 用户唯一标识（为空表示任务级模板或统一试卷）
     */
    private String userIdentify;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷内容
     */
    private String examPaperContent;

    /**
     * 试卷总分
     */
    private Float examPaperScore;

    /**
     * 及格分
     */
    private Float gradeScore;

    /**
     * 题目数量
     */
    private Integer requestNumber;

    /**
     * 描述
     */
    private String examPaperRemark;

    public ExamTaskPaper(Long id, String name, String examPaperContent) {
        this.id = id;
        this.name = name;
        this.examPaperContent = examPaperContent;
    }
}
