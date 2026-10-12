package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command.level;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class IncreaseAlarmLevelPlayConfigCommand {

    /**
     * 报警级别编号
     */
    @NotBlank(message = "报警级别不能为空")
    private String alarmLevelCode;

    /**
     * 是否循环播放
     */
    private Boolean looped;

    /**
     * 播放次数
     */
    private Integer playNum;

    /**
     * 播放文件
     */
    private Long file;
}
