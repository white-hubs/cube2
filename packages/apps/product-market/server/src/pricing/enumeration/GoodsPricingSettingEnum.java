package com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration;

import lombok.Getter;

/**
 * 采价相关设置
 */
public enum GoodsPricingSettingEnum {

    NOTICE_DELAY_MINUTE("采价任务延迟通知分钟数");

    @Getter
    String label;

    GoodsPricingSettingEnum(String label) {
        this.label = label;
    }

}
