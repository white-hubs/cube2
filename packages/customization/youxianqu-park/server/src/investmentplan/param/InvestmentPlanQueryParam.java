package com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 招商计划条件对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvestmentPlanQueryParam extends Paging {
    /**
     * 目标类型(PlanTargetTypeEnum)
     * 1购地 2租赁 3销售
     */
    private String targetType;
    
    /**
     * 计划月份
     */
    private String planMonth;
}
