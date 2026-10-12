package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.aggregates;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.valueobj.ExamTaskUserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExamTaskRecord {

    /**
     * 考试记录主键
     */
    private Long id;

    /**
     * 考试任务主键
     */
    private Long examTaskId;

    /**
     * 试卷信息
     */
    private ExamPaper examPaper;

    /**
     * 考试用户信息
     */
    private ExamTaskUserInfo examTaskUserInfo;

    /**
     * 是否通过
     */
    private Boolean passed;

    /**
     * 的分数
     */
    private Float score;

    /**
     * 考试时间
     */
    private LocalDateTime examTime;

    /**
     * 考试提交时间
     */
    private LocalDateTime examSubmittedTime;

    /**
     * 考试耗时
     */
    private Integer examDuration;

    /**
     * 租户编号
     */
    private Integer tenant;

    public UserExamTaskRecord(
            Long id,
            Long examTaskId,
            Boolean passed,
            Float score,
            LocalDateTime examTime,
            LocalDateTime examSubmittedTime,
            Integer examDuration
                             ) {
        this.id = id;
        this.examTaskId = examTaskId;
        this.passed = passed;
        this.score = score;
        this.examTime = examTime;
        this.examSubmittedTime = examSubmittedTime;
        this.examDuration = examDuration;
    }

    /**
     * 考试用户设置
     */
    public void settingExamTaskUserInfo(ExamTaskUserInfo examTaskUserInfo) {
        this.examTaskUserInfo = examTaskUserInfo;
    }

    /**
     * 考卷设置
     *
     * @param examPaper
     */
    public void settingExamPaperInfo(ExamPaper examPaper) {
        this.examPaper = examPaper;
    }

    /**
     * 租户设置
     *
     * @param tenant
     */
    public void setTenant(Integer tenant) {
        this.tenant = tenant;
    }
}
