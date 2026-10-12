package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 请假单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_ask_leave_from")
@EqualsAndHashCode(callSuper = true)
public class AttAskLeaveFromPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long applicantId;
    private String applicantName;
    private Long deptId;
    private String deptName;
    private String postIds;
    private String postName;
    private String phone;
    // 入职日期
    private LocalDateTime enterDate;
    private Long holidayId;
    private String holidayName;
    // 是否计入缺勤(true 表示计入缺勤
    private Boolean intoAbsence;
    private Integer holidayDayOfHour;
    // 假期余额
    private Long holidayBalance;
    private LocalDateTime leaveStartTime;
    private LocalDateTime leaveEndTime;
    // 请假时长
    private Long leaveNumber;
    // 休假地址
    private String leaveLocation;
    // 请假备注
    private String leaveRemark;
    // 文件的主键
    private Long fileKey;
    // 请假单(审批)状态
    private String status;
    // 扣除的记录id
    private String deadlineIds;
    // 销假单id
    private Long cancelLeaveFromId;
    // 流程实例id
    private String processInstanceId;
}
