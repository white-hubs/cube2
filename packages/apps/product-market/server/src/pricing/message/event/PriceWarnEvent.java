package com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRecord;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 采价预警发布事件
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class PriceWarnEvent extends ApplicationEvent {
    /**
     * 采价任务id
     */
    private Long priceTaskId;

    private GoodsPriceWarnRecord warnRecord;

    public PriceWarnEvent(Object source, Long priceTaskId, GoodsPriceWarnRecord warnRecord) {
        super(source);
        this.priceTaskId = priceTaskId;
        this.warnRecord = warnRecord;
    }

}
