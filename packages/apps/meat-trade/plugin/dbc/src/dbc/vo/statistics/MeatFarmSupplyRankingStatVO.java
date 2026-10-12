package com.guhecloud.cube.sn.meattrade.plugin.dbc.vo.statistics;

import com.guhecloud.platform.base.standard.jackson.annotation.Decimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeatFarmSupplyRankingStatVO {

    /**
     * 屠宰场名称
     */
    private String abattoirName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 供货片数
     */
    private Integer supplySlices;

    /**
     * 供货重量
     */
    @Decimal
    private BigDecimal supplyWeight;

    /**
     * 环比
     */
    @Decimal
    private BigDecimal qoq;
}
