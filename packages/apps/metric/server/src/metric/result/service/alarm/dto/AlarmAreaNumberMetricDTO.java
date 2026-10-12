package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmAreaNumberMetricDTO {

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域报警数量
     */
    private Integer areaAlarmNumber;
}
