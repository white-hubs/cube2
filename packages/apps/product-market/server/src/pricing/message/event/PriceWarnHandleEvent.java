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
public class PriceWarnHandleEvent extends ApplicationEvent {

    private GoodsPriceWarnRecord warnRecord;

    public PriceWarnHandleEvent(Object source, GoodsPriceWarnRecord warnRecord) {
        super(source);
        this.warnRecord = warnRecord;
    }

}
