package com.guhecloud.cube.biz.meattrade.server.third.response.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeatTestingRankingStatResponse {

    /**
     * 供货商报备总数
     */
    private Integer supplyReports;

    /**
     * 检疫票不合格主键
     */
    private List<Long> pratiqueFailIds;

    /**
     * 检疫票不合格数量
     */
    private Integer pratiqueFailed;

    /**
     * 检疫票不合格百分比
     */
    @Decimal
    private BigDecimal pratiqueFailedPer;

    /**
     * 检疫票不合格环比
     */
    @Decimal
    private BigDecimal qoqPratiqueFailed;

    /**
     * 瘦肉精不合格
     */
    private Integer clenbuterolFailed;

    /**
     * 瘦肉精不合格百分比
     */
    @Decimal
    private BigDecimal clenbuterolFailedPer;

    /**
     * 瘦肉精不合格环比
     */
    @Decimal
    private BigDecimal qoqClenbuterolFailed;

    /**
     * 含水量不合格
     */
    private Integer watered;

    /**
     * 含水量不合格百分比
     */
    @Decimal
    private BigDecimal wateredPer;

    /**
     * 含水量不合格环比
     */
    @Decimal
    private BigDecimal qoqWatered;
}
