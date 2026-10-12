package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试任务统计
 *
 * @author lanchun
 * @date 2023/4/21 9:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamTaskStatisticsVO {

    /**
     * 考试任务数
     */
    private Integer totalNum;

    /**
     * 环比率
     */
    private Float chainRatio;

    /**
     * 缺考人数
     */
    private Integer missExamUserNumber;

    /**
     * 不及格人数
     */
    private Integer noPassedUserNumber;
}
