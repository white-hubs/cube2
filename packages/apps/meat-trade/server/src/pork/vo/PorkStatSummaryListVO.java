package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderDateWeightVO;
import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lanchun
 * @date 2023/8/2 14:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PorkStatSummaryListVO {

    /**
     * 本期重量
     */
    @Decimal
    private BigDecimal currentWeight;

    /**
     * 本期数据
     */
    private List<OrderDateWeightVO> currentDateWeightList;

    /**
     * 上期重量
     */
    @Decimal
    private BigDecimal lastWeight;

    /**
     * 上期数据
     */
    private List<OrderDateWeightVO> lastDateWeightList;

    /**
     * 环比值
     */
    @Decimal
    private BigDecimal qoqTradeWeight;
}
