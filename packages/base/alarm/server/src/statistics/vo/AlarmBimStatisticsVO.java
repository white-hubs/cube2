package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警统计返回参数
 *
 * @author lanchun
 * @date 2023/3/16 13:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimStatisticsVO {

    /**
     * 报警总数
     */
    private Long alarmNum;

    /**
     * 已处理报警数
     */
    private Long alarmHandlerNum;

    /**
     * 真实报警数
     */
    private Long alarmActualNum;

    /**
     * 误报数量
     */
    private Long alarmImpracticalNum;
}
