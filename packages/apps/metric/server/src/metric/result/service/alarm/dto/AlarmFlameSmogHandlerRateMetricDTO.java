package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmFlameSmogHandlerRateMetricDTO {

    /**
     * 明火烟雾总处理数量
     */
    private Integer flameSmogAlarmHandlerNumber;

    /**
     * 明火烟雾报警处理率
     */
    private Double flameSmogAlarmHandlerRate;
}
