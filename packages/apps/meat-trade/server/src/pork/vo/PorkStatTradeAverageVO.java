package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 白条交易均价
 *
 * @author lzy
 * @date 2021/11/24
 */
@Data
public class PorkStatTradeAverageVO {

    /**
     * 今日每kg交易均价
     */
    @Decimal
    private BigDecimal todayAverageUnitAmount;

    /**
     * 昨日每kg交易均价
     */
    @Decimal
    private BigDecimal yesterdayAverageUnitAmount;

    /**
     * 每kg均价相比昨天
     */
    @Decimal
    private BigDecimal comparedWithYesterdayUnitAmount;

    /**
     * 这周每kg交易均价
     */
    @Decimal
    private BigDecimal thisWeekAverageUnitAmount;

    /**
     * 上周每kg交易均价
     */
    @Decimal
    private BigDecimal lastWeekAverageUnitAmount;

    /**
     * 每kg均价相比上周同期
     */
    @Decimal
    private BigDecimal comparedWithLastWeekUnitAmount;

    /**
     * 这月每kg交易均价
     */
    @Decimal
    private BigDecimal thisMonthAverageUnitAmount;

    /**
     * 上月每kg交易均价
     */
    @Decimal
    private BigDecimal lastMonthAverageUnitAmount;

    /**
     * 每kg均价相比上月同期
     */
    @Decimal
    private BigDecimal comparedWithLastMonthUnitAmount;

    /**
     * 近30天交易均价
     */
    @Decimal
    private BigDecimal recentThirtyDayAverageUnitAmount;

}
