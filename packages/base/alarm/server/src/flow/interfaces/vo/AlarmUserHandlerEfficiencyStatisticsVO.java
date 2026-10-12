package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户处理报警效率
 *
 * @author lanchun
 * @date 2022/11/10 16:29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmUserHandlerEfficiencyStatisticsVO {

    private String alarmHandlerBy;

    /**
     * 用户名称
     */
    private String alarmHandlerName;

    /**
     * 平均耗时
     */
    private Double avgProcessMinutes;

    /**
     * 平均耗时秒数
     */
    private Long avgProcessSeconds;
}
