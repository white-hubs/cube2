package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 出差台账
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@TableName("att_away_business_ledger")
public class AttAwayBusinessLedgerPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long staffId;
    private String staffName;
    private Long deptId;
    private String deptName;
    private Long groupId;
    // 出差时间
    private Long awayNumber;
    // 出差日期
    private LocalDateTime awayDate;
    // 出差单id
    private Long awayFromId;

}
