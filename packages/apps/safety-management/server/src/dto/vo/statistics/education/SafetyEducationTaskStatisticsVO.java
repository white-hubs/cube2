package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.education;

import lombok.Data;

@Data
public class SafetyEducationTaskStatisticsVO {

    /**
     * 考试任务数量
     */
    private Integer examTaskNum;

    /**
     * 培训任务数量
     */
    private Integer cultivateTaskNum;
}
