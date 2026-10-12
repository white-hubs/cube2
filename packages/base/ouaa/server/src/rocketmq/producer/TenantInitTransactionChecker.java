package com.guhecloud.cube.biz.ouaa.server.rocketmq.producer;

import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.TenantInitMessage;
import com.guhecloud.cube.biz.ouaa.server.service.TenantService;
import com.guhecloud.platform.rocketmq.producer.RocketMQTransactionChecker;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author lzy
 * @date 2021/12/22
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantInitTransactionChecker implements RocketMQTransactionChecker<TenantInitMessage> {

    private final TenantService tenantService;

    @Override
    public RocketMQLocalTransactionState check(Message<?> message, TenantInitMessage payload) {
        Tenant tenant = this.tenantService.getByCode(payload.getTenant());
        return tenant != null ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }

    @Override
    public String getTopic() {
        return TenantInitMessage.TOPIC;
    }

    @Override
    public Class<TenantInitMessage> getPayloadClass() {
        return TenantInitMessage.class;
    }
}
