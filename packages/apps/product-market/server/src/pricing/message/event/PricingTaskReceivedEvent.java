package com.guhecloud.cube.srv.gen.product.market.server.pricing.message.event;

import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskDetail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 采价任务接受事件
 *
 * @author appet
 * @date 2023/4/19 20:05
 */
@Getter
public class PricingTaskReceivedEvent extends ApplicationEvent {

    private GoodsPricingTask pricingTask;
    private GoodsPricingTaskDetail pricingTaskDetail;
    private String sceneCode;
    private Map<String, String> msgParams;

    public PricingTaskReceivedEvent(Object source, GoodsPricingTask pricingTask, GoodsPricingTaskDetail pricingTaskDetail, String sceneCode,
                                    Map<String, String> msgParams
                                   ) {
        super(source);
        this.pricingTask = pricingTask;
        this.pricingTaskDetail = pricingTaskDetail;
        this.sceneCode = sceneCode;
        this.msgParams = msgParams;
    }

}
