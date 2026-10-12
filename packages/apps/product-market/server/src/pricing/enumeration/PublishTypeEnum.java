package com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration;

import lombok.Getter;

/**
 * 任务发布类型
 */
public enum PublishTypeEnum {

    EVERY_DAY("每天"),

    EVERY_WEEK("每周"),

    EVERY_MONTH("每月");

    @Getter
    String label;

    PublishTypeEnum(String label) {
        this.label = label;
    }

}
