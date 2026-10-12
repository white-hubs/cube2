package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmFlameSmogHandlerEfficiencyMetricDTO {

    /**
     * 平均处理时长
     */
    private Double flameSmogAlarmAvgHandlerDuration;

    /**
     * 最大处理时长
     */
    private Double maxHandlerDuration;

    /**
     * 最小处理时长
     */
    private Double minHandlerDuration;
}
