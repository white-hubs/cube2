package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 16:40
 * @Description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeadFsHandlerEfficiencyMetricDTO {
    /**
     * 平均处理时长
     */
    private Double flameSmogAlarmAvgHandlerDuration;

    /**
     * 最大处理时长
     */
    private MarketHandlerDuration maxHandlerDurationMarket;

    /**
     * 最小处理时长
     */
    private MarketHandlerDuration minHandlerDurationMarket;

}
