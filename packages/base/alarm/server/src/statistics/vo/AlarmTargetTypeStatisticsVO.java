package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.Data;

/**
 * 报警对象类型统计返回参数
 *
 * @author lanchun
 * @date 2023/12/19 17:03
 */
@Data
public class AlarmTargetTypeStatisticsVO {

    /**
     * 设备类型主键
     */
    private String alarmTargetTypeId;

    /**
     * 报警数量
     */
    private Integer alarmNum;
}
