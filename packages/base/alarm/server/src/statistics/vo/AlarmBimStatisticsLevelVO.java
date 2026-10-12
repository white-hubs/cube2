package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警级别统计
 *
 * @author lanchun
 * @date 2023/3/16 13:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimStatisticsLevelVO {

    /**
     * 报警级别编号
     */
    private String code;

    /**
     * 报警级别名称
     */
    private String name;

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 占比
     */
    private double chainRatio;
}
