package com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 采价任务发布事件
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class PricingTaskHandleEvent extends ApplicationEvent {

    private GoodsPricingRecord goodsPricingRecord;

    public PricingTaskHandleEvent(Object source, GoodsPricingRecord goodsPricingRecord) {
        super(source);
        this.goodsPricingRecord = goodsPricingRecord;
    }

}
