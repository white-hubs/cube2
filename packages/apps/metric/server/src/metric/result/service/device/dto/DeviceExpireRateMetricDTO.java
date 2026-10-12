package com.guhecloud.cube.biz.metric.server.metric.result.service.device.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceExpireRateMetricDTO {

    /**
     * 到期数
     */
    private Integer expireNum;

    /**
     * 到期率
     */
    private BigDecimal expireRate;
}
