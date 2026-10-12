package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lanchun
 * @date 2025/6/16 13:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadOneLevelMarketHandlerEfficiencyMetricDTO {

    /**
     * 报警编号
     */
    private String code;

    /**
     * 级别名称
     */
    private String name;

    /**
     * 处理效率最高市场平均处理时长
     */
    private Double handlerHighEfficiencyMarketAvgDuration;

    /**
     * 处理率最高用户市场名称
     */
    private String handlerHighEfficiencyMarketName;

    /**
     * 处理效率最低用户平均处理时长
     */
    private Double handlerLowEfficiencyMarketAvgDuration;

    /**
     * 处理率最低用户市场名称
     */
    private String handlerLowEfficiencyMarketName;
}
