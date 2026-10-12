package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/11/21 9:46
 * @description 预扣资源
 */
@Getter
@Setter
@AllArgsConstructor
public class WithholdCommand {
    /**
     * 请假单id
     */
    private Long leaveFormId;
}
