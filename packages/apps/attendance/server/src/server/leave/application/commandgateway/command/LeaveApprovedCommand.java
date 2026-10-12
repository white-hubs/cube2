package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author pengjian
 * @date 2022/11/18 19:51
 * @description 请假审批通过
 */
@Getter
@AllArgsConstructor
public class LeaveApprovedCommand {
    /**
     * 请假单id
     */
    private Long leaveFormId;
}
