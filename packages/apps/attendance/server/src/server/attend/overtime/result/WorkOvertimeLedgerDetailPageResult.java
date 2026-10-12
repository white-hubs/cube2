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
public class WorkOvertimeLedgerDetailPageResult {
    private Long id;
    /**
     * 用户名称
     */
    private Long userId;
    private String userName;
    /**
     * 加班类型
     */
    private String workType;
    /**
     * 加班开始时间
     */
    private LocalDateTime startTime;
    /**
     * 加班结束时间
     */
    private LocalDateTime endTime;
    /**
     * 加班时长
     */
    private BigDecimal actualDuration;
}
