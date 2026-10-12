package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class GoodsPriceTrendStatVO {
    private Long goodsCategoryId;
    private String goodsCategoryName;
    private String date;//可能是 天 月  年
    //最高价 平均价
    private BigDecimal maxPriceAvg;
    private BigDecimal midPriceAvg;
    private BigDecimal minPriceAvg;

    public GoodsPriceTrendStatVO(String date, BigDecimal maxPriceAvg, BigDecimal midPriceAvg, BigDecimal minPriceAvg) {
        this.date = date;
        this.maxPriceAvg = maxPriceAvg;
        this.midPriceAvg = midPriceAvg;
        this.minPriceAvg = minPriceAvg;
    }

    public GoodsPriceTrendStatVO(String date) {
        this.date = date;
        this.maxPriceAvg = BigDecimal.ZERO;
        this.midPriceAvg = BigDecimal.ZERO;
        this.minPriceAvg = BigDecimal.ZERO;
    }

    public GoodsPriceTrendStatVO(String date, String name) {
        this.date = date;
        this.goodsCategoryName = name;
        this.maxPriceAvg = BigDecimal.ZERO;
        this.midPriceAvg = BigDecimal.ZERO;
        this.minPriceAvg = BigDecimal.ZERO;
    }
}
