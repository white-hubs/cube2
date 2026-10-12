package com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration;

import lombok.Getter;

/**
 * 紧急程度
 */
public enum UrgencyEnum {

    MAX("紧急"),

    MID("普通"),

    MIN("较低");

    @Getter
    String label;

    UrgencyEnum(String label) {
        this.label = label;
    }

}
