package com.guhecloud.cube.biz.meattrade.server.base.enumeration;

import lombok.Getter;

/**
 * @author lanchun
 * @date 2023/8/8 15:58
 */
@Getter
public enum MeatGoodsFreshLabelEnum {

    /**
     * 冷鲜
     */
    COLD_FRESH("冷鲜"),

    /**
     * 热鲜
     */
    HOT_FRESH("热鲜"),

    /**
     * 冻品
     */
    FROZEN("冻品");

    /**
     * 描述信息
     */
    private final String description;

    MeatGoodsFreshLabelEnum(String description) {
        this.description = description;
    }
}
