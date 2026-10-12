package com.guhecloud.cube.srv.gen.energymanagement.server.rocketmq.consumer;

import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.FuelDischargeStandardService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听新增租户
 * 初始化租户
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = TenantInitMessage.TOPIC + "_ENERGY_TENANT_INIT",
        topic = TenantInitMessage.TOPIC,
        selectorType = SelectorType.TAG
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyInitTenantMessageConsumer implements RocketMQListener<TenantInitMessage> {
    private final FuelDischargeStandardService fuelDischargeStandardService;

    @Override
    public void onMessage(TenantInitMessage tenantInitMessage) {
        SessionContextHolder.requestTenant(tenantInitMessage.getTenant());
        // 插入燃料类型初始数据
        fuelDischargeStandardService.insertDefaultFuel();
        SessionContextHolder.clearSessionContext();
    }

}
