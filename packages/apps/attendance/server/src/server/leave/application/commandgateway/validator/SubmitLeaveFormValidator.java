package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.validator;

import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;

/**
 * @author pengjian
 * @date 2022/11/18 18:55
 * @description 提交请假单验证器
 */
public interface SubmitLeaveFormValidator {
    /**
     * 验证
     */
    boolean verify(LeaveForm leaveForm);
}
