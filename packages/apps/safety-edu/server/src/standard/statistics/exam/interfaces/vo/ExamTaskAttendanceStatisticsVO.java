package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo;

import lombok.Data;

/**
 * 考试任务出勤情况统计
 *
 * @author lanchun
 * @date 2023/4/20 20:09
 */
@Data
public class ExamTaskAttendanceStatisticsVO {

    private Long id;

    /**
     * 考试任务名称
     */
    private String name;

    /**
     * 预计考试人数
     */
    private Integer examUserNumber;

    /**
     * 缺考人数
     */
    private Integer missExamUserNumber;

    /**
     * 不及格人数
     */
    private Integer noPassedUserNumber;
}
