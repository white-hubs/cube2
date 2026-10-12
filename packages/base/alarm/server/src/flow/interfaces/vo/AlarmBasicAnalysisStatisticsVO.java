package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报警基本分析返回参数
 *
 * @author lanchun
 * @date 2022/11/14 9:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBasicAnalysisStatisticsVO {

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 处理数
     */
    private Long handlerNum;

    /**
     * 处理时长
     */
    private Double handlerTime;

    /**
     * 环比率
     */
    private String ringRatio;

    /**
     * 同比率
     */
    private String sameRatio;
}
