package com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.result;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 招商计划返回对象
 */
@Data
public class InvestmentPlanResult {
    private Long id;
    /**
     * 计划月份
     */
    private String planMonth;

    /**
     * 计划完成目标
     */
    private Integer planTotal;

    /**
     * 目标类型
     * 1购地 2租赁 3销售
     */
    private String targetType;

    /**
     * 完成结果
     * 完成结果：0、达标，1、不达标
     */
    private Integer planResult;

    /**
     * 完成百分比
     */
    private BigDecimal planPercent;
}
