package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmOneLevelUserHandlerEfficiencyMetricDTO {

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
     * 处理效率最低用户名
     */
    private String handlerLowEfficiencyUserName;

    /**
     * 处理效率最低用户平均处理时长
     */
    private Double handlerLowEfficiencyUserAvgDuration;
}
