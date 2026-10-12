package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警趋势统计
 *
 * @author lanchun
 * @date 2023/3/16 14:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimStatisticsTrendVO {

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 时间(等同于横坐标)
     */
    private String period;
}
