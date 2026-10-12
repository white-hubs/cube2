package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-18  16:24
 * @Description:
 * @Version: 1.0
 */
@Data
@TableName("work_overtime_ledger")
public class WorkOvertimeLedger extends BasicEnhancedTenantEntity<Long> {

    private Long id;
    private Long userId;
    private String userName;
    private Long orgId;
    private LocalDateTime workDate;
    private BigDecimal workDuration;
    private BigDecimal workPayDuration;
    private BigDecimal restDuration;
    private BigDecimal restPatDuration;
    private BigDecimal festDuration;
    private BigDecimal festOatDuration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String workType;
    //申请时间
    private LocalDateTime applyTime;
    //申请处理完成时间
    private LocalDateTime applyDoneTime;
    /**
     * 备注
     */
    private String remarks;
}
