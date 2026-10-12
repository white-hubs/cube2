package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmHandlerEfficiencyMetricDTO {

    /**
     * 报警平均处理时长
     */
    private Double alarmAvgHandlerDuration;
}
