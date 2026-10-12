package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计报警数量,未处理数量,超时报警数量返回参数
 *
 * @author lanchun
 * @date 2023/3/16 14:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimStatisticsRecordStatusVO {

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 报警总数环比率
     */
    private float chainRatio;

    /**
     * 未处理报警数量
     */
    private Long alarmUnresolvedHandlerNum;

    /**
     * 未处理报警数环比率
     */
    private float alarmUnresolvedHandlerChainRatio;

    /**
     * 超时未处理报警数量
     */
    private Long alarmTimeoutNum;

    /**
     * 已处理报警数
     */
    private Long alarmHandlerNum;

    /**
     * 已处理报警数环比
     */
    private float alarmHandlerChainRatio;
}
