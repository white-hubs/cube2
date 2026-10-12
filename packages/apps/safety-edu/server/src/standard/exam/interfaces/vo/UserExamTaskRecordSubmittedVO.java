package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户考试记录提交返回参数
 *
 * @author lanchun
 * @date 2023/4/20 11:22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserExamTaskRecordSubmittedVO {

    private Long id;

    /**
     * 试卷分数
     */
    private Float paperScore;

    /**
     * 是否通过
     */
    private Boolean passed;

    /**
     * 的分数
     */
    private Float score;

    /**
     * 正确率
     */
    private Float passingRate;
}
