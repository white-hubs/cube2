package com.guhecloud.cube.srv.gen.energymanagement.server.rocketmq.consumer;

import com.guhecloud.cube.base.device.center.metadata.lang.Const;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.rocketmq.event.ProductChangeEvent;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = Const.PRODUCT_CHANGE_TOPIC + "_energy_product_update",
                         topic = Const.PRODUCT_CHANGE_TOPIC, selectorType = SelectorType.TAG
)
public class EnergyUpdateProductMessageConsumer implements RocketMQListener<ProductChangeEvent> {

    private final MeterDeviceMapper meterDeviceMapper;

    @Override
    public void onMessage(ProductChangeEvent message) {
        log.info("receive productChangeEvent: {}", message.toString());

        SecurityContextHolder.setPrincipal(Principal.builder().account("deviceCenterMq").name("deviceCenterMq").type(Principal.Type.CLIENT).build());
        meterDeviceMapper.updateMeterProduct(message.getProductId(), message.getNewBrandName());
    }
}

