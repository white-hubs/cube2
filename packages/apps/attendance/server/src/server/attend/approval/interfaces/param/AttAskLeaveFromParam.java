package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Data
public class AttAskLeaveFromParam {
    private Long id;
    @NotNull(message = "申请人不能为空")
    private Long applicantId;
    private String applicantName;
    private Long deptId;
    private String deptName;
    // 入职日期
    private LocalDateTime enterDate;
    @NotNull(message = "请假类型不能为空")
    private Long holidayId;
    private String holidayName;
    // 假期余额
    private Float holidayBalance;
    @NotNull(message = "请假开始时间不能为空")
    private LocalDateTime leaveStartTime;
    @NotNull(message = "请假结束时间不能为空")
    private LocalDateTime leaveEndTime;
    // 请假时长
    @Min(value = 0, message = "请假时长必须大于0")
    private Float leaveNumber;
    // 休假地址
    @NotNull(message = "休假地址不能为空")
    private String leaveLocation;
}
