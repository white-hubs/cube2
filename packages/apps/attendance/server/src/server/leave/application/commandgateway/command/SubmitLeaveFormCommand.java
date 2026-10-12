package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.entity.LeaveFormDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/11/18 19:47
 * @description
 */
@Getter
@Setter
@Builder
public class SubmitLeaveFormCommand {
    /**
     * 请假单名称 (一般是请假人名称与时间的组合)
     */
    private String name;

    /**
     * 请假起止时间
     */
    private LeaveFormDetail.LeaveTime leaveTime;

    /**
     * 请假单提交时间，默认当前时间
     */
    private LocalDateTime submitTime;

    /**
     * 请假单提交用户，默认当前用户
     */
    private Long submitUserId;

    private Holiday.HolidayType leaveType;

    private String location;
}
