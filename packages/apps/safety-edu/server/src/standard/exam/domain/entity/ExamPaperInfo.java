package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 试卷基本信息
 *
 * @author lanchun
 * @date 2023/4/18 16:26
 */
@Data
@NoArgsConstructor
public class ExamPaperInfo {

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷分数
     */
    private Float score;

    /**
     * 及格分数
     */
    private Float passingGradeScore;

    /**
     * 试卷说明
     */
    private String remark;

    public ExamPaperInfo(String name, Float score, Float passingGradeScore, String remark) {
        this.name = name;
        this.score = score;
        this.passingGradeScore = passingGradeScore;
        this.remark = remark;
    }
}
