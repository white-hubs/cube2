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
 * 离岗单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_leave_post_from")
@EqualsAndHashCode(callSuper = true)
public class AttLeavePostFromPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long applicantId;
    private String applicantName;
    private Long deptId;
    private String deptName;
    private String postIds;
    private String postName;
    private Long postRuleId;
    private String postRuleName;
    private String phone;
    private LocalDateTime postStartTime;
    private LocalDateTime postEndTime;
    // 离岗时长
    private Long postNumber;
    // 离岗备注
    private String postRemark;
    // 文件的主键
    private Long fileKey;
    // 离岗单(审批)状态
    private String status;
    // 流程实例id
    private String processInstanceId;

}
