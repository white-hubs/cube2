package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmLevelHandlerRateMetricDTO {

    /**
     * 报警级别编号
     */
    private String code;

    /**
     * 报警级别名称
     */
    private String name;

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 处理数量
     */
    private Long handlerNum;

    /**
     * 处理率
     */
    private Float handlerRate;
}
