package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警设备分析返回参数
 *
 * @author lanchun
 * @date 2022/11/9 16:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDeviceAnalysisStatisticsVO {

    /**
     * 排名前三设备名称
     */
    private String alarmTopThreeDeviceNames;

    /**
     * 排名第一设备名称
     */
    private String alarmTopDeviceName;

    /**
     * 排名第一设备数量
     */
    private Integer alarmTopNum;

    /**
     * 设备分组统计
     */
    private List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS;
}
