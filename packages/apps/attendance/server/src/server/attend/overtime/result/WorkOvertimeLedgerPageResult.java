package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-14  19:16
 * @Description:
 * @Version: 1.0
 */
@Data
public class WorkOvertimeLedgerPageResult {
    private Long id;
    /**
     * 用户名称
     */
    private Long userId;
    private String userName;
    /**
     * 部门名称
     */
    private Long orgId;
    private String orgName;
    /**
     * 加班时间
     */
    private LocalDateTime workDate;
    /**
     * 工作日时长（时）
     */
    private BigDecimal workDuration;
    /**
     * 工作时长记为加班费时长
     */
    private BigDecimal workPayDuration;
    /**
     * 休息日加班时长
     */
    private BigDecimal restDuration;
    /**
     * 休息日加班记加班费时长
     */
    private BigDecimal restPatDuration;
    /**
     * 节假日加班时长
     */
    private BigDecimal festDuration;
    /**
     * 节假日记加班费时长
     */
    private BigDecimal festOatDuration;
}
