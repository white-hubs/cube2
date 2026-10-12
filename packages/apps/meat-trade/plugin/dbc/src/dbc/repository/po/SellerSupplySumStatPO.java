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
 * 供货数据统计信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("seller_supply_sum_stat")
public class SellerSupplySumStatPO extends BasicEnhancedTenantEntity<Long> {

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
     * 供货片数
     */
    private Integer totalTallyQuantity;

    /**
     * 供货重量
     */
    private BigDecimal totalTallyWeight;

    /**
     * 理货总数（来货总数）
     */
    private Integer totalTallyCount;

    /**
     * 供货商户数
     */
    private Integer totalSellerCount;

    /**
     * 上周期供货片数
     */
    private Integer prevTotalTallyQuantity;

    /**
     * 上周期供货重量
     */
    private BigDecimal prevTotalTallyWeight;

    /**
     * 上周期理货总数（来货总数）
     */
    private Integer prevTotalTallyCount;

    /**
     * 上周期供货商户数
     */
    private Integer prevSellerCount;

    /**
     * 供货片数环比
     */
    private String tallyQuantityRate;

    /**
     * 供货重量环比
     */
    private String tallyWeightRate;

    /**
     * 理货数环比
     */
    private String tallyCountRate;

    /**
     * 供货商户数环比
     */
    private String sellerCountRate;
}
