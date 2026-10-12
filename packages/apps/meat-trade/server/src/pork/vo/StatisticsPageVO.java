package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售统计信息
 */
@Data
public class StatisticsPageVO {
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
     * 商户名称
     */
    private String supplierName;

    /**
     * 商户编码
     */
    private String supplierCode;

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
    private int porkArrangeSlices;

    /**
     * 成交片数
     */
    private int dealSlices;

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
     * 实际收入
     */
    @Decimal
    private BigDecimal actualAmount;

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
     * 商品品类
     */
    private String goodsCategoryId;

    /**
     * 商品品类名称
     */
    private String goodsCategoryName;
}
