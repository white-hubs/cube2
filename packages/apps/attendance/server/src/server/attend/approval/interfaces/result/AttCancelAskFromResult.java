package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.interfaces.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 可销假的请假单返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttCancelAskFromResult {
    // 请假单id
    private Long id;
    // 请假类型id
    private Long holidayRuleId;
    // 请假开始时间
    private LocalDateTime leaveStartTime;
    // 请假结束时间
    private LocalDateTime leaveEndTime;
    // 流引擎申请编号
    private String flowFromNo;
    // 流引擎申请名称
    private String flowInstanceName;
    // 流引擎发起时间
    private LocalDateTime flowSubmitTime;
    // 流引擎状态名称
    private String flowStatus;

}
