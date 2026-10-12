package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 指标设置;
 *
 * @author : lijie
 * @date : 2024-3-20
 */
@TableName("rule_norm")
@Data
public class RuleNorm extends BasicEnhancedTenantEntity<Long> {
    /**
     *
     */
    @TableId
    private Long id;
    /**
     *
     */
    private Long ruleId;
    /**
     * 指标编码
     */
    private String normCode;
    /**
     *
     */
    private String normName;
    /**
     *
     */
    private String unit;
    /**
     * 是否展示
     */
    private Integer display;
    /**
     * 上限
     */
    private BigDecimal upperLimit;
    /**
     * 下限
     */
    private BigDecimal lowerLimit;
    /**
     * 展示区域
     */
    private String showArea;
    /**
     * 排序
     */
    private Integer sort;
}