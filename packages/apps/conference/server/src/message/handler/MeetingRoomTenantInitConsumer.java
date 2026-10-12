package com.guhecloud.cube.srv.gen.conferencemanage.server.message.handler;

import com.guhecloud.cube.biz.ouaa.client.message.TenantInitMessage;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.FieldConfigurationService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/13 14:04
 * @Description: 新增租户初始化配置
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TenantInitMessage.TOPIC + "_CONFERENCE", topic = TenantInitMessage.TOPIC)
public class MeetingRoomTenantInitConsumer implements RocketMQListener<TenantInitMessage> {

    private final FieldConfigurationService fieldConfigurationService;

    @Override
    public void onMessage(TenantInitMessage message) {
        SessionContextHolder.clearSessionContext();
        SessionContextHolder.requestTenant(message.getTenant());
        fieldConfigurationService.initTenantConfig(message.getTenant());
        SessionContextHolder.clearSessionContext();
    }
}