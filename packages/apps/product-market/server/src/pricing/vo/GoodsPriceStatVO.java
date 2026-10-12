package com.guhecloud.cube.srv.gen.product.market.server.pricing.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class GoodsPriceStatVO {
    private Long goodsCategoryId;
    private String goodsCategoryName;
    //来货量
    private BigDecimal incomingQuantity;
    //最高价 平均价
    private BigDecimal maxPriceAvg;
    private BigDecimal midPriceAvg;
    private BigDecimal minPriceAvg;
    //环比
    private BigDecimal qoqMaxPrice;
    private BigDecimal qoqMidPrice;
    private BigDecimal qoqMinPrice;
    //同比
    private BigDecimal yoyMaxPrice;
    private BigDecimal yoyMidPrice;
    private BigDecimal yoyMinPrice;

    //环比
    private BigDecimal qoqMaxPricePert;
    private BigDecimal qoqMidPricePert;
    private BigDecimal qoqMinPricePert;
    //同比
    private BigDecimal yoyMaxPricePert;
    private BigDecimal yoyMidPricePert;
    private BigDecimal yoyMinPricePert;

    public void calQoQ(BigDecimal maxPrice, BigDecimal midPrice, BigDecimal minPrice) {
        this.qoqMaxPrice = maxPrice;
        this.qoqMidPrice = midPrice;
        this.qoqMinPrice = minPrice;
        if (maxPriceAvg != null && maxPrice != null && maxPriceAvg.intValue() != 0 && maxPrice.intValue() != 0) {
            qoqMaxPricePert = maxPriceAvg.subtract(maxPrice).multiply(BigDecimal.valueOf(100).divide(maxPrice, 2, RoundingMode.HALF_UP));
        } else if (maxPrice == null || maxPrice.intValue() == 0) {
            qoqMaxPricePert = null;
        } else {
            qoqMaxPricePert = BigDecimal.ZERO;
        }

        if (midPriceAvg != null && midPrice != null && midPriceAvg.intValue() != 0 && midPrice.intValue() != 0) {
            qoqMidPricePert = midPriceAvg.subtract(midPrice).multiply(BigDecimal.valueOf(100).divide(midPrice, 2, RoundingMode.HALF_UP));
        } else if (midPrice == null || midPrice.intValue() == 0) {
            qoqMidPricePert = null;
        } else {
            qoqMidPricePert = BigDecimal.ZERO;
        }

        if (minPriceAvg != null && minPrice != null && minPriceAvg.intValue() != 0 && minPrice.intValue() != 0) {
            qoqMinPricePert = minPriceAvg.subtract(minPrice).multiply(BigDecimal.valueOf(100).divide(minPrice, 2, RoundingMode.HALF_UP));
        } else if (minPrice == null || minPrice.intValue() == 0) {
            qoqMinPricePert = null;
        } else {
            qoqMinPricePert = BigDecimal.ZERO;
        }

    }

    public void calYoY(BigDecimal maxPrice, BigDecimal midPrice, BigDecimal minPrice) {
        this.yoyMaxPrice = maxPrice;
        this.yoyMidPrice = midPrice;
        this.yoyMinPrice = minPrice;
        if (maxPriceAvg != null && maxPrice != null && maxPriceAvg.intValue() != 0 && maxPrice.intValue() != 0) {
            yoyMaxPricePert = maxPriceAvg.subtract(maxPrice).multiply(BigDecimal.valueOf(100).divide(maxPrice, 2, RoundingMode.HALF_UP));
        } else if (maxPrice == null || maxPrice.intValue() == 0) {
            yoyMaxPricePert = null;
        } else {
            yoyMaxPricePert = BigDecimal.ZERO;
        }

        if (midPriceAvg != null && midPrice != null && midPriceAvg.intValue() != 0 && midPrice.intValue() != 0) {
            yoyMidPricePert = midPriceAvg.subtract(midPrice).multiply(BigDecimal.valueOf(100).divide(midPrice, 2, RoundingMode.HALF_UP));
        } else if (midPrice == null || midPrice.intValue() == 0) {
            yoyMidPricePert = null;
        } else {
            yoyMidPricePert = BigDecimal.ZERO;
        }

        if (minPriceAvg != null && minPrice != null && minPriceAvg.intValue() != 0 && minPrice.intValue() != 0) {
            yoyMinPricePert = minPriceAvg.subtract(minPrice).multiply(BigDecimal.valueOf(100).divide(minPrice, 2, RoundingMode.HALF_UP));
        } else if (minPrice == null || minPrice.intValue() == 0) {
            yoyMinPricePert = null;
        } else {
            yoyMinPricePert = BigDecimal.ZERO;
        }
    }
}
