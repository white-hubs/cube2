package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 白条供货量信息
 *
 * @author lzy
 * @date 2021/11/24
 */
@Data
public class PorkStatSupplyCapacityVO {

    /**
     * 今天供货数量
     */
    private Integer todaySupplySlices;

    /**
     * 昨天供货片数
     */
    private Integer yesterdaySupplySlices;

    /**
     * 今天供货重量
     */
    @Decimal
    private BigDecimal todaySupplyWeight;

    /**
     * 昨天供货重量
     */
    @Decimal
    private BigDecimal yesterdaySupplyWeight;

    /**
     * 相比昨天供货片数
     */
    private Integer comparedWithYesterdaySlices;

    /**
     * 相比昨天供货重量
     */
    @Decimal
    private BigDecimal comparedWithYesterdayWeight;

    /**
     * 本周供货片数
     */
    private Integer thisWeekSupplySlices;

    /**
     * 上周同期供货片数
     */
    private Integer lastWeekSupplySlices;

    /**
     * 本周供货重量
     */
    @Decimal
    private BigDecimal thisWeekSupplyWeight;

    /**
     * 上周同期供货重量
     */
    @Decimal
    private BigDecimal lastWeekSupplyWeight;

    /**
     * 相比上周供货片数
     */
    private Integer comparedWithLastWeekSlices;

    /**
     * 相比上周供货重量
     */
    @Decimal
    private BigDecimal comparedWithLastWeekWeight;

    /**
     * 本月供货片数
     */
    private Integer thisMonthSupplySlices;

    /**
     * 上月同期供货片数
     */
    private Integer lastMonthSupplySlices;

    /**
     * 本月供货重量
     */
    @Decimal
    private BigDecimal thisMonthSupplyWeight;

    /**
     * 上月同期供货重量
     */
    @Decimal
    private BigDecimal lastMonthSupplyWeight;

    /**
     * 相比上月供货片数
     */
    private Integer comparedWithLastMonthSlices;

    /**
     * 相比上月供货重量
     */
    @Decimal
    private BigDecimal comparedWithLastMonthWeight;

    /**
     * 最近30天供货片数
     */
    private Integer recentThirtyDaysSupplySlices;

    /**
     * 最近30天供货重量
     */
    @Decimal
    private BigDecimal recentThirtyDaysSupplyWeight;
}
