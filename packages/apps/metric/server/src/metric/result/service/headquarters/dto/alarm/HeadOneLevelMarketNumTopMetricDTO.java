package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lanchun
 * @date 2025/6/16 13:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadOneLevelMarketNumTopMetricDTO {

    /**
     * 一级报警最高市场列表
     */
    private List<MarketAlarmNum> oneLevelAlarmNumTopMarkets;

    /**
     * 一级报警最低市场列表
     */
    private List<MarketAlarmNum> oneLevelAlarmNumLowMarkets;
}
