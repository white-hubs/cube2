package com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration;

import lombok.Getter;

/**
 * 任务发布类型
 */
public enum PriceTypeEnum {

    MAX("最高价格"),

    MID("中间价格"),

    MIN("最低价格"),

    MAX_INCREASE("最高价格涨幅"),

    MID_INCREASE("中间价格涨幅"),

    MIN_INCREASE("最低价格涨幅"),

    MAX_DECREASE("最高价格降幅"),

    MID_DECREASE("中间价格降幅"),

    MIN_DECREASE("最低价格降幅");

    @Getter
    String label;

    PriceTypeEnum(String label) {
        this.label = label;
    }

}
