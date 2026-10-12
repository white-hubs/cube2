package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 报警统计返回参数
 *
 * @author lanchun
 * @date 2022/6/7 14:20
 */
@Builder
@Data
public class AlarmStatisticsVO {

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 报警处理数量
     */
    private Long alarmHandlerNum;

    /**
     * 环比
     */
    private String alarmRingRatio;

    /**
     * 报警处理状态统计
     */
    private List<AlarmStatusStatisticsVO> alarmStatusStatisticsVOS;

    /**
     * 报警处理结果统计
     */
    private List<AlarmResultStatisticsVO> alarmResultStatisticsVOS;
}
