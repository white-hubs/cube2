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
 * 白条来货数据统计信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("meat_farm_supply_ranking_stat")
public class MeatFarmSupplyRankingStatPO extends BasicEnhancedTenantEntity<Long> {

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
     * 屠宰场编号
     */
    private String meatFarmCode;

    /**
     * 屠宰场名称
     */
    private String meatFarmName;

    /**
     * 上周期供货片数
     */
    private Integer prevTotalTallyQuantity;

    /**
     * 供货重量
     */
    private BigDecimal prevTotalTallyWeight;

    /**
     * 供货片数环比
     */
    private String tallyQuantityRate;

    /**
     * 供货重量环比
     */
    private String tallyWeightRate;

    /**
     * 供货商户数环比
     */
    private String sellerCountRate;
}
