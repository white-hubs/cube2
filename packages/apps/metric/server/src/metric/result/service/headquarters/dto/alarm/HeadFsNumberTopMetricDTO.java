package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lanchun
 * @date 2025/6/16 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadFsNumberTopMetricDTO {

    /**
     * 明火烟雾数高市场
     */
    private List<MarketAlarmNum> flameSmogHighMarkets;

    /**
     * 明火烟雾数低市场
     */
    private List<MarketAlarmNum> flameSmogLowMarkets;
}
