package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警趋势返回参数
 *
 * @author lanchun
 * @date 2022/7/12 14:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmTrendStatisticsVO {

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 已处理报警数量
     */
    private Long handlerNum;

    /**
     * 时间
     */
    private String period;

    public AlarmTrendStatisticsVO(Long alarmNum, String period) {
        this.alarmNum = alarmNum;
        this.period = period;
    }

    public AlarmTrendStatisticsVO(String period) {
        this.period = period;
    }
}
