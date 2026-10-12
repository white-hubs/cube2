package com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceHealthLevelMetricDTO {

    /**
     * 设备健康度指标
     */
    private Double healthLevel;

    /**
     * 低于60数量
     */
    private Integer healthBelowSixtyNumber;

    /**
     * 低于60占比
     */
    private Double healthBelowSixtyRate;
}
