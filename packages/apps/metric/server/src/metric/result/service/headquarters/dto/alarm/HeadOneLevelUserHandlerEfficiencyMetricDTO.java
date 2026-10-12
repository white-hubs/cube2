package com.guhecloud.cube.biz.metric.server.metric.result.service.headquarters.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/13 17:55
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeadOneLevelUserHandlerEfficiencyMetricDTO {

    /**
     * 报警编号
     */
    private String code;

    /**
     * 级别名称
     */
    private String name;

    /**
     * 处理效率最高用户名
     */
    private String handlerHighEfficiencyUserName;

    /**
     * 处理效率最高用户平均处理时长
     */
    private Double handlerHighEfficiencyUserAvgDuration;

    /**
     * 处理率最高用户市场名称
     */
    private String handlerHighEfficiencyUserMarketName;

    /**
     * 处理效率最低用户名
     */
    private String handlerLowEfficiencyUserName;

    /**
     * 处理效率最低用户平均处理时长
     */
    private Double handlerLowEfficiencyUserAvgDuration;

    /**
     * 处理率最低用户市场名称
     */
    private String handlerLowEfficiencyUserMarketName;
}
