package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.init;

import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.init.service.RiskControlTenantInitService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 风险管控租户开通初始化监听器。
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TenantInitMessage.TOPIC + "_RISK_CONTROL", topic = TenantInitMessage.TOPIC)
public class RiskControlTenantInitConsumer implements RocketMQListener<TenantInitMessage> {
    private final RiskControlTenantInitService tenantInitService;

    @Override
    public void onMessage(TenantInitMessage message) {
        if (message == null || message.getTenant() == null) {
            log.warn("风险管控租户初始化消息缺少租户编号");
            return;
        }
        SessionContextHolder.clearSessionContext();
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            tenantInitService.initTenantDefaults(message.getTenant());
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
