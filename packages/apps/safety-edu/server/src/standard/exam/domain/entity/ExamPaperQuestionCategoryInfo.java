package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 试卷大题实体
 *
 * @author lanchun
 * @date 2023/4/18 16:45
 */
@Data
@NoArgsConstructor
public class ExamPaperQuestionCategoryInfo {

    private Long id;

    /**
     * 大题标题
     */
    private String title;

    /**
     * 大题序号
     */
    private Integer sort;

    /**
     * 题目集合
     */
    private List<ExamPaperQuestionInfo> examPaperQuestionInfos;

    public ExamPaperQuestionCategoryInfo(Long id, String title, Integer sort, List<ExamPaperQuestionInfo> examPaperQuestionInfos) {
        this.id = id;
        this.title = title;
        this.sort = sort;
        this.examPaperQuestionInfos = examPaperQuestionInfos;
    }

    /**
     * 设置题目
     */
    public void settingExamPaperQuestionInfos(List<ExamPaperQuestionInfo> examPaperQuestionInfos) {
        this.examPaperQuestionInfos = examPaperQuestionInfos;
    }
}
