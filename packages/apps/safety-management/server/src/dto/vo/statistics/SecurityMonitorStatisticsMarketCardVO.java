package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.Data;

/**
 * @author wql
 * @describe 安防监控市场统计卡片
 * @date 2023/9/8 15:23
 */
@Data
public class SecurityMonitorStatisticsMarketCardVO {
    /**
     * 安防监控报警数
     */
    private Integer securityMonitorNum;
    /**
     * 安防监控报警数环比
     */
    private Double securityMonitorRatio;
    /**
     * 明火报警数
     */
    private Integer openFlameAlarmNum;
    /**
     * 明火报警数环比
     */
    private Double openFlameAlarmRatio;
    /**
     * 烟雾报警数
     */
    private Integer smokeAlarmNum;
    /**
     * 烟雾报警数环比
     */
    private Double smokeAlarmRatio;
    /**
     * 电动车入电梯报警数
     */
    private Integer electromobileAlermNum;
    /**
     * 电动车入电梯报警数环比
     */
    private Double electromobileAlermRatio;
    /**
     * 占用消防通道报警数
     */
    private Integer channelBlockingAlarmNum;
    /**
     * 占用消防通道报警数环比
     */
    private Double channelBlockingAlarmRatio;
}
