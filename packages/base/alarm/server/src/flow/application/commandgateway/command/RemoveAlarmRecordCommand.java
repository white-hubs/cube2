package com.guhecloud.cube.base.alarm.server.flow.application.commandgateway.command;

import lombok.Data;

import java.util.Set;

@Data
public class RemoveAlarmRecordCommand {

    /**
     * 报警记录主键集合
     */
    private Set<Long> ids;
}
