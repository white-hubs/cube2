package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmHandlerRateMetricDTO {

    /**
     * 总处理数量
     */
    private Integer alarmHandlerNumber;

    /**
     * 报警处理率
     */
    private Double alarmHandlerRate;
}
