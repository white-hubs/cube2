package com.guhecloud.cube.biz.meattrade.server.pork.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 统计信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("meattrade_statistics")
public class Statistics extends BasicEnhancedEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商户主键
     */
    private Long supplierId;

    /**
     * 批次号
     */
    private String porkReportCode;

    /**
     * 平均价格
     */
    @Decimal
    private BigDecimal averagePrice;

    /**
     * 最高价
     */
    @Decimal
    private BigDecimal highestPrice;

    /**
     * 最低价
     */
    @Decimal
    private BigDecimal lowerPrice;

    /**
     * 理货片数
     */
    private Integer porkArrangeSlices;

    /**
     * 成交片数
     */
    private Integer dealSlices;

    /**
     * 理货重量
     */
    @Decimal
    private BigDecimal porkArrangeWeight;

    /**
     * 成交重量
     */
    @Decimal
    private BigDecimal dealWeight;

    /**
     * 成交金额
     */
    @Decimal
    private BigDecimal amount;

    /**
     * 佣金
     */
    @Decimal
    private BigDecimal commission;

    /**
     * 报备时间
     */
    private LocalDateTime statisticsDate;

    /**
     * 租户编号
     */
    private Integer tenant;

}
