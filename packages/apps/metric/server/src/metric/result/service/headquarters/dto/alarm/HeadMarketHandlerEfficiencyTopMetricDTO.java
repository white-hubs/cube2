package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 17:43
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadMarketHandlerEfficiencyTopMetricDTO {

    /**
     * 处理效率最高市场
     */
    private List<MarketHandlerDuration> topHandlerMarkets;

    /**
     * 处理效率最低用市场
     */
    private List<MarketHandlerDuration> lowHandlerMarkets;

}
