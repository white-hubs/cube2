package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import lombok.Data;

@Data
public class SettingAlarmBusinessSnapshotCommand {

    /**
     * 报警主键
     */
    private Long id;

    /**
     * 业务快照信息
     */
    private String businessSnapshot;
}
