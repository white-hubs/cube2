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
 * 离岗台账
 * 拆分成天的
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_leave_post_ledger")
@EqualsAndHashCode(callSuper = true)
public class AttLeavePostLedgerPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long staffId;
    private String staffName;
    private Long deptId;
    private String deptName;
    private Long groupId;
    // 离岗时间
    private Long postNumber;
    // 请假日期
    private LocalDateTime postDate;
    // 离岗单id
    private Long postFromId;
}
