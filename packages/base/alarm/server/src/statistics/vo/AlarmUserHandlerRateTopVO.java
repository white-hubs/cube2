package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmUserHandlerRateTopVO {

    /**
     * 用户处理报警数
     */
    private Long userHandlerAlarmNumber;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String alarmHandlerUserName;

    /**
     * 用户处理率
     */
    private Double userHandlerRate;
}
