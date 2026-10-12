package com.guhecloud.cube.biz.daily.task.task.result;

import lombok.Data;

/**
 * 网格异常任务统计结果
 */
@Data
public class GridAbnormalTaskCountResult {

    /**
     * 网格区域id
     */
    private Long gridAreaId;

    /**
     * 异常任务数
     */
    private Long abnormalTaskCount;
}
