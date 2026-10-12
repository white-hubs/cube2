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
 * 出差申请单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_away_business_From")
@EqualsAndHashCode(callSuper = true)
public class AttAwayBusinessFromPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long applicantId;
    private String applicantName;
    private Long deptId;
    private String deptName;
    private String postIds;
    private String postName;
    private Long awayRuleId;
    private String awayRuleName;
    private String phone;
    private LocalDateTime awayStartTime;
    private LocalDateTime awayEndTime;
    // 出差时长
    private Long awayNumber;
    // 出差备注
    private String awayRemark;
    // 文件的主键
    private Long fileKey;
    // 出差单(审批)状态
    private String status;
    // 流程实例id
    private String processInstanceId;
}
