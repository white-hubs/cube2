package com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SellerSupplySumStatVO {

    /**
     * 供货商数量
     */
    private Integer supplier;

    /**
     * 供货商数量环比
     */
    @Decimal
    private BigDecimal qoqSupplier;

    /**
     * 供货重量
     */
    @Decimal
    private BigDecimal supplyWeight;

    /**
     * 供货商重量环比
     */
    @Decimal
    private BigDecimal qoqSupplyWeight;

    /**
     * 供货片数
     */
    private Integer supplySlices;

    /**
     * 供货片数环比
     */
    @Decimal
    private BigDecimal qoqSupplySlices;
}
