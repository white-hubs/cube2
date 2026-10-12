package com.guhecloud.cube.srv.gen.attendance.management.server.leavepost.commandgateway.commond;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeavePostCommand {
    private Long applicantId;//申请人ID

    private Long applicantDeptId;//部门ID

    private String postIds;//职位ID

    private String phone;//联系电话

    private LocalDateTime startTime;//离岗时间

    private LocalDateTime endTime;//返岗时间

    private String reason;//外出地点

    private LocalDateTime applyTime;//申请时间

    private LocalDateTime applyDoneTime;//审批完成时间
}
