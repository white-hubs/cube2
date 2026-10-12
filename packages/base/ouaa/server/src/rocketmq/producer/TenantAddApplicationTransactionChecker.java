package com.guhecloud.cube.biz.ouaa.server.rocketmq.producer;

import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.TenantAddApplicationMessage;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.platform.rocketmq.producer.RocketMQTransactionChecker;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 租户新增应用事务消息
 *
 * @author lanchun
 * @date 2023/7/4 9:24
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TenantAddApplicationTransactionChecker implements RocketMQTransactionChecker<TenantAddApplicationMessage> {

    private final TenantApplicationService tenantApplicationService;

    @Override
    public RocketMQLocalTransactionState check(Message<?> message, TenantAddApplicationMessage tenantAddApplicationMessage) {
        TenantApplication tenantApplication = this.tenantApplicationService.getById(tenantAddApplicationMessage.getId());
        return Objects.nonNull(tenantApplication) ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }

    @Override
    public String getTopic() {
        return TenantAddApplicationMessage.TOPIC;
    }

    @Override
    public Class<TenantAddApplicationMessage> getPayloadClass() {
        return TenantAddApplicationMessage.class;
    }
}
