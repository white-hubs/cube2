package com.guhecloud.cube.sn.meattrade.plugin.dbc.repository.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 白条交易大户统计信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sellers_comparison_top_stat")
public class SellersComparisonTopStatPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 时间范围
     */
    private String timeRange;

    /**
     * 周期开始时间
     */
    private LocalDateTime startDate;

    /**
     * 周期结束时间
     */
    private LocalDateTime endDate;

    /**
     * 交易净重
     */
    private BigDecimal totalNet;

    /**
     * 交易实际金额
     */
    private BigDecimal totalPayable;

    /**
     * maybe是商家id
     */
    private String seller;

    /**
     * 商家名称
     */
    private String sellerName;

    /**
     * 上周期交易净重
     */
    private BigDecimal prevTotalNet;

    /**
     * 上周期交易实际金额
     */
    private BigDecimal prevTotalPayable;

    /**
     * 交易净重环比
     */
    private String netRate;

    /**
     * 交易实际金额环比
     */
    private String payableRate;
}
