package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatisticsVO {

    /**
     * 任务数统计
     */
    private TaskNumberStatisticsVO taskNumberStatistics;

    /**
     * 任务状态统计
     */
    private TaskStatusStatisticsVO taskStatusStatistics;

    /**
     * 任务线路信息
     */
    private TaskLineStatisticsVO taskLineStatistics;
}
