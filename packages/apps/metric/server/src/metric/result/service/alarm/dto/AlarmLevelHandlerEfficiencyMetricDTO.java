package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLevelHandlerEfficiencyMetricDTO {

    /**
     * 报警编号
     */
    private String code;

    /**
     * 级别名称
     */
    private String name;

    /**
     * 已处理报警数量
     */
    private Long handlerNum;

    /**
     * 总时长
     */
    private Long durationTotal;

    /**
     * 平均处理时长
     */
    private Float avgHandlerDuration;
}
