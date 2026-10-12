package com.guhecloud.cube.base.alarm.server.analysis.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SafetyAnalysisAlarmVO {

    /**
     * 安全报警数
     */
    private Long alarmNum;
    private Float ringAlarmNumRatio;

    /**
     * 未处理报警数
     */
    private Long noHandlerNum;
    private Float ringNoHandlerRatio;

    /**
     * 已处理报警数
     */
    private Long handlerNum;
    private Float ringHandlerNumRatio;

    /**
     * 用气安全-易燃设备建档数
     */

    private Long inflammableDeviceNum;
}
