package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadDeviceExpireRateMetricDTO {

    /**
     * 设备总数
     */
    private Integer totalDeviceNumber;

    /**
     * 到期数
     */
    private Integer expireNum;

    /**
     * 到期率
     */
    private Double expireRate;

    /**
     * 市场设备到期率统计
     */
    private List<MarketDeviceExpireRate> marketDeviceExpireRates;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MarketDeviceExpireRate {

        /**
         * 市场编号
         */
        private Integer marketCode;

        /**
         * 市场名称
         */
        private String marketName;

        /**
         * 到期数
         */
        private Integer expireNum;

        /**
         * 到期率
         */
        private BigDecimal expireRate;
    }

}
