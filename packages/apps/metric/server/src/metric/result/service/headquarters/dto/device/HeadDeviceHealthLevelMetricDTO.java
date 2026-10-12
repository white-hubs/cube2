package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadDeviceHealthLevelMetricDTO {

    /**
     * 总设备数
     */
    private Integer totalDeviceNumber;

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

    /**
     * 低于60的市场列表
     */
    private List<MarketDeviceAvgHealth> belowSixtyMarkets;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MarketDeviceAvgHealth {

        /**
         * 市场编号
         */
        private Integer marketCode;

        /**
         * 市场名称
         */
        private String marketName;

        /**
         * 设备健康度指标
         */
        private Double healthLevel;
    }
}
