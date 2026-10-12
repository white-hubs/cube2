package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 明火烟雾市场处理率排名
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 17:14
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeadFsMarketHandlerRateTopMetricDTO {

    /**
     * 明火烟雾处理率最高的市场
     */
    private List<MarketHandlerRate> topFlameSmogHandlerMarkets;

    /**
     * 明火烟雾处理率最低市场
     */
    private List<MarketHandlerRate> lowFlameSmogHandlerMarkets;
}
