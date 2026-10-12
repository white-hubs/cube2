package com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;
import java.util.Set;

/**
 * 采价任务发布事件
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class PricingTaskEvent extends ApplicationEvent {

    private GoodsPricingTask pricingTask;
    private Set<Long> userIds;
    private String sceneCode;
    private Map<String, String> msgParams;

    public PricingTaskEvent(Object source, GoodsPricingTask pricingTask, Set<Long> userIds, String sceneCode,
                            Map<String, String> msgParams
                           ) {
        super(source);
        this.pricingTask = pricingTask;
        this.userIds = userIds;
        this.sceneCode = sceneCode;
        this.msgParams = msgParams;
    }

}
