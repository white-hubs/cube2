package com.guhecloud.cube.biz.meattrade.server.pork.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 销售统计信息
 */
@Data
public class StatisticsDTO {
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
    private Double averagePrice;

    /**
     * 最高价
     */
    private Double highestPrice;

    /**
     * 最低价
     */
    private Double lowerPrice;

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
    private Double porkArrangeWeight;

    /**
     * 成交重量
     */
    private Double dealWeight;

    /**
     * 成交金额
     */
    private Double amount;

    /**
     * 实际金额
     */
    private Double actualAmount;

    /**
     * 佣金
     */
    private Double commission;

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
