package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 17:17
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadLoosWorksReformRateDTO {

    /**
     * 整改率前三名
     */
    List<MarketReformRate> marketReformRateTopThree;
    /**
     * 整改率后三名
     */
    List<MarketReformRate> marketReformRateLastThree;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MarketReformRate {
        /**
         * 市场名称
         */
        private String marketName;

        /**
         * 整改数
         */
        private Long reformNum;
        /**
         * 整改率
         */
        private Double reformRate;
    }
}
