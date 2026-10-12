package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警处理状态统计
 *
 * @author lanchun
 * @date 2022/6/7 14:12
 */
//@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmStatusStatisticsVO {

    /**
     * 处理状态
     */
    private String alarmStatus;

    /**
     * 处理状态名称
     */
    private String alarmStatusName;

    /**
     * 数量
     */
    private Long alarmNum;

    /**
     * 占比
     */
    private float ratio;

    /**
     * 环比
     */
    private float chainRatio;

    public AlarmStatusStatisticsVO(String alarmStatus, String alarmStatusName, Long alarmNum) {
        this.alarmStatus = alarmStatus;
        this.alarmStatusName = alarmStatusName;
        this.alarmNum = alarmNum;
    }
}
