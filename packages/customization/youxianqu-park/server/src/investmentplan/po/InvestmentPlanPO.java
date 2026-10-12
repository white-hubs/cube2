package com.guhecloud.cube.customized.youxianqu.park.management.server.investmentplan.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 招商计划数据模型实体
 *
 * @author lanchun
 * @date 2024/8/28 16:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode
@TableName(value = "ZHYQ_ZSYZ_TARGETS_PLANS")
public class InvestmentPlanPO {

    /**
     * 顺序号
     */
    @TableField(value = "ID")
    private Long id;

    /**
     * 目标表ID
     */
    @TableField(value = "TARGETSID")
    private Long targetsId;

    /**
     * 计划月份
     */
    @TableField(value = "PLAN_MONTH")
    private String planMonth;

    /**
     * 计划完成目标
     */
    @TableField(value = "PLAN_TOTAL")
    private Integer planTotal;

    /**
     * 计划描述
     */
    @TableField(value = "PLAN_DESC")
    private String planDesc;

    /**
     * 执行人
     */
    @TableField(value = "ZX_PERSON")
    private Long zxPerson;

    /**
     * 执行部门
     */
    @TableField(value = "ZX_DEPT")
    private Long zxDept;

    /**
     * 单位
     */
    @TableField(value = "PLANUNIT")
    private String planUnit;

    /**
     * 开始
     */
    @TableField(value = "START_DATE")
    private LocalDateTime startDate;

    /**
     * 结束
     */
    @TableField(value = "END_DATE")
    private LocalDateTime endDate;

    /**
     * 目标类型
     * 1购地 2租赁 3销售
     */
    @TableField(value = "TARGET_TYPE")
    private String targetType;

    /**
     * 完成结果
     * 完成结果：0、达标，1、不达标
     */
    @TableField(value = "PLAN_RESULT")
    private Integer planResult;

    /**
     * 完成百分比
     */
    @TableField(value = "PLAN_PERCENT")
    private BigDecimal planPercent;

}
