package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmImpracticalRateMetricDTO {

    /**
     * 报警真实数量
     */
    private Long alarmActualNumber;

    /**
     * 报警误报数量
     */
    private Long alarmImpracticalNumber;

    /**
     * 报警误报占比
     */
    private Double alarmImpracticalRate;
}
