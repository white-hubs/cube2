package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengjian
 * @date 2022/11/18 19:55
 * @description 请假拒绝
 */
@Getter
@AllArgsConstructor
public class LeaveRefuseCommand {
    /**
     * 请假单id
     */
    private Long leaveFormId;
}
