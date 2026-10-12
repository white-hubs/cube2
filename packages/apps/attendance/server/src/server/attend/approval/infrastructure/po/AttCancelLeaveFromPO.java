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
 * 销假单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_cancel_leave_from")
@EqualsAndHashCode(callSuper = true)
public class AttCancelLeaveFromPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long applicantId;
    private String applicantName;
    private Long deptId;
    private String deptName;
    private Long holidayId;
    private String holidayName;
    // 关联的请假单对象（json
    private String askLeaveFrom;
    // 请假单id
    private Long askLeaveFromId;
    private LocalDateTime cancelStartTime;
    private LocalDateTime cancelEndTime;
    // 销假时长
    private Long cancelNumber;
    // 销假备注
    private String cancelRemark;
    // 文件的主键
    private Long fileKey;
    // 销假单(审批)状态
    private String status;
    // 流程实例id
    private String processInstanceId;

}
