package com.guhecloud.cube.biz.metric.server.metric.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/7 10:13
 * @Description:
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketHandlerRateLevel {

    /**
     * 市场名称
     */
    private String marketName;
    /**
     * 报警数最多的等级
     */
    private String maxAlarmLevel;

    /**
     * 报警最多等级的报警数
     */
    private Long maxAlarmCount;

    /**
     * 处理率最高的报警等级
     */
    private String maxHandlerLevel;

    /**
     * 处理最高的报警等级对应的处理率
     */
    private Float maxHandlerRate;

    /**
     * 处理效率最高的报警等级
     */
    private String maxEfficiencyLevel;

    /**
     * 处理效率最高的报警等级对应的处理率
     */
    private Float maxEfficiencyRate;
}
