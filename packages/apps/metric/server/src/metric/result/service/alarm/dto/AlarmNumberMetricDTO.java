package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmNumberMetricDTO {

    /**
     * 报警总数
     */
    private Integer alarmNumber;
}
