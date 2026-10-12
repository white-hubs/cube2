package com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.validator.time.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class EmergencyTimeInfo {

    /**
     * 开始时间
     */
    private LocalTime beginTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;
}
