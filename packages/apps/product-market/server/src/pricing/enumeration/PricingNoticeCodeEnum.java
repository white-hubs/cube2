package com.guhecloud.cube.srv.gen.product.market.server.pricing.enumeration;

import lombok.Getter;

/**
 * 商品采价消息通知类型(消息场景code)
 */
@Getter
public enum PricingNoticeCodeEnum {
    PRICING_TASK_PUBLISHED_NOTIFY("任务发放"),
    PRICING_TASK_RECEIVED_NOTIFY("超时未接收"),
    PRICING_TASK_COMPLETE_NOTIFY("超时未完成"),
    ;
    final String describe;

    PricingNoticeCodeEnum(String describe) {
        this.describe = describe;
    }
}
