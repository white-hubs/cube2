package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2022/11/21
 * @description 加班明细表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOvertimeDetailsParam {
    private String subDataId;
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
    private BigDecimal workDuration;
    /**
     * 补偿方案
     */
    private String compensate;
    /**
     * 加班原因
     */
    private String reason;
    /**
     * 加班时段
     */
    private String workPeriod;
    /**
     * 加班日期
     */
    private String workDate;

}