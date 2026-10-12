package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmEventTypeHandlerRateMetricDTO {

    /**
     * 报警类型编号
     */
    private String alarmTypeCode;

    /**
     * 报警类型名称
     */
    private String alarmTypeName;

    /**
     * 报警类型总数
     */
    private Integer alarmTypeNumber;

    /**
     * 报警类型处理数量
     */
    private Integer alarmTypeHandlerNumber;

    /**
     * 报警类型占比
     */
    private Double alarmTypeHandlerRate;

}
