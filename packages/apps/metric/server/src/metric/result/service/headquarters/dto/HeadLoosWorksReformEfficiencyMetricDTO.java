package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/17 17:34
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadLoosWorksReformEfficiencyMetricDTO {

    /**
     * 整改效率前三名
     */
    private List<MarketReformEfficiency> reformEfficiencyTopThree;

    /**
     * 整改率后三名
     */
    private List<MarketReformEfficiency> reformEfficiencyLowThree;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MarketReformEfficiency {
        /**
         * 市场名称
         */
        private String marketName;
        /**
         * 整改效率
         */
        private Double reformEfficiency;
    }
}
