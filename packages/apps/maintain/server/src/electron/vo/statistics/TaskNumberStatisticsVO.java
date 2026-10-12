package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务数量统计
 *
 * @author lanchun
 * @date 2024/1/25 10:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskNumberStatisticsVO {

    /**
     * 任务数量
     */
    private Long totalNumber;

    /**
     * 环比
     */
    private float chainRatio;
}
