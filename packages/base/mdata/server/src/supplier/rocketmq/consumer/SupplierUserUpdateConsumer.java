package com.guhecloud.cube.biz.mdata.server.supplier.rocketmq.consumer;

import com.guhecloud.cube.biz.mdata.server.supplier.service.SupplierService;
import com.guhecloud.cube.biz.ouaa.client.message.UserUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "supplier_" + UserUpdateMessage.TOPIC,
                         topic = UserUpdateMessage.TOPIC
)
public class SupplierUserUpdateConsumer implements RocketMQListener<UserUpdateMessage> {

    private final SupplierService supplierService;

    @Override
    public void onMessage(UserUpdateMessage message) {
        this.supplierService.updateSupplierAdminUser(message.getId(), message.getName());
    }
}
