package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.Data;

/**
 * @author wql
 * @describe 电气安全市场统计卡片
 * @date 2023/9/8 15:41
 */
@Data
public class ElectricSafetyStatisticsMarketCardVO {
    /**
     * 电气报警数
     */
    private Integer electricAlarmNum;
    /**
     * 电气报警数环比
     */
    private Double electricAlarmRatio;
    /**
     * 过流报警数
     */
    private Integer overFlowAlarmNum;
    /**
     * 过流报警数环比
     */
    private Double overFlowAlarmRatio;
    /**
     * 过压报警数
     */
    private Integer overVoltageAlarmNum;
    /**
     * 过压报警数环比
     */
    private Double overVoltageAlarmRatio;
    /**
     * 漏电报警数
     */
    private Integer electricLeakageAlarmNum;
    /**
     * 漏电报警数环比
     */
    private Double electricLeakageAlarmRatio;
    /**
     * 欠压报警数
     */
    private Integer underVoltageAlarmNum;
    /**
     * 欠压报警数环比
     */
    private Double underVoltageAlarmATIO;
}
