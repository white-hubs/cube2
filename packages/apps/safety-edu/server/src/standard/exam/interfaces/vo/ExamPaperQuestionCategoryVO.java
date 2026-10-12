package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 试卷大题返回参数
 *
 * @author lanchun
 * @date 2023/4/19 11:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamPaperQuestionCategoryVO {

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
    private List<ExamPaperQuestionVO> examQuestions;
}
