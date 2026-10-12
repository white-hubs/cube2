package com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectronErrorRateMetricDTO {
    private Long electronAbnormalNumber;
    private Long electronTotal;
    private BigDecimal electronAbnormalRate;
}
