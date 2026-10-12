package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 猪肉滑轨白条交易量
 *
 * @author lzy
 * @date 2021/11/24
 */
@Data
public class PorkStatTradeCapacityVO {

    /**
     * 今天交易片数
     */
    private Integer todayTradeSlices;

    /**
     * 昨天交易片数
     */
    private Integer yesterdayTradeSlices;

    /**
     * 今天交易重量
     */
    @Decimal
    private BigDecimal todayTradeWeight;

    /**
     * 昨天交易重量
     */
    @Decimal
    private BigDecimal yesterdayTradeWeight;

    /**
     * 相比昨天交易片数
     */
    private Integer comparedWithYesterdaySlices;

    /**
     * 相比昨天交易重量
     */
    @Decimal
    private BigDecimal comparedWithYesterdayWeight;

    /**
     * 本周交易片数
     */
    private Integer thisWeekTradeSlices;

    /**
     * 上周同期交易片数
     */
    private Integer lastWeekTradeSlices;

    /**
     * 本周交易重量
     */
    @Decimal
    private BigDecimal thisWeekTradeWeight;

    /**
     * 上周同期交易重量
     */
    @Decimal
    private BigDecimal lastWeekTradeWeight;

    /**
     * 相比上周交易片数
     */
    private Integer comparedWithLastWeekSlices;

    /**
     * 相比上周交易重量
     */
    @Decimal
    private BigDecimal comparedWithLastWeekWeight;

    /**
     * 本月交易片数
     */
    private Integer thisMonthTradeSlices;

    /**
     * 上月同期交易片数
     */
    private Integer lastMonthTradeSlices;

    /**
     * 本月交易重量
     */
    @Decimal
    private BigDecimal thisMonthTradeWeight;

    /**
     * 上月同期交易重量
     */
    @Decimal
    private BigDecimal lastMonthTradeWeight;

    /**
     * 相比上月交易片数
     */
    private Integer comparedWithLastMonthSlices;

    /**
     * 相比上月交易重量
     */
    @Decimal
    private BigDecimal comparedWithLastMonthWeight;

    /**
     * 最近30天交易片数
     */
    private Integer recentThirtyDaysTradeSlices;

    /**
     * 最近30天交易重量
     */
    @Decimal
    private BigDecimal recentThirtyDaysTradeWeight;

}
