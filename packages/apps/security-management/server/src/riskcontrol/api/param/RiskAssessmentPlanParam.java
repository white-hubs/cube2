package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RiskAssessmentPlanParam {
    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 评估计划名称。
     */
    private String planName;

    /**
     * 评估计划周期类型。
     * 取值建议：DAY/DAILY=每日，WEEK/WEEKLY=每周，MONTH/MONTHLY=每月，
     * QUARTER/QUARTERLY=每季度，HALF_YEAR/HALF_YEARLY=每半年，YEAR/YEARLY=每年。
     */
    private String cycleType;

    /**
     * 下次执行时间。
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextExecuteTime;

    /**
     * 评估计划通知人员用户 ID 集合。
     */
    private List<String> notifyUserIds;
}
