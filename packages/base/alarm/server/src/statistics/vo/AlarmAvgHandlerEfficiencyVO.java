package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmAvgHandlerEfficiencyVO {

    /**
     * 平均处理时长
     */
    private Float avgHandlerDuration;

    /**
     * 最长处理时长
     */
    private Long maxHandlerDuration;

    /**
     * 最短处理时长
     */
    private Long minHandlerDuration;
}
