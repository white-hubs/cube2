package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskAssessmentPlanPageParam extends Paging {
    /**
     * 关键词，按计划名称模糊匹配。
     */
    private String keyword;

    /**
     * 风险单元 ID。
     */
    private Long unitId;

    /**
     * 计划状态筛选，取值：ENABLED=启用，DISABLED=禁用。
     */
    private String status;

    /**
     * 周期类型筛选，取值建议：DAY/DAILY、WEEK/WEEKLY、MONTH/MONTHLY、QUARTER/QUARTERLY、
     * HALF_YEAR/HALF_YEARLY、YEAR/YEARLY。
     */
    private String cycleType;

    /**
     * 通知人用户 ID 筛选。
     */
    private String notifyUserId;
}
