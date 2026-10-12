package com.guhecloud.cube.biz.metric.server.metric.result.service.electron.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 巡更总数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElectronTotalMetricDTO {

    /**
     * 巡更总数
     */
    private Long electronTotal;
}
